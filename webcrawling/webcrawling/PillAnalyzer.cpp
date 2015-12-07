#include "PillAnalyzer.h"

PillAnalyzer::PillAnalyzer() {
	
}

Pill PillAnalyzer::analyzePill(unsigned char* imageBuf, int size) {
	Mat img, src_gray, src_HSV;
	int R, G, B;
	int H;

	vector<unsigned char> imgData;
	for (int i = 0; i < size; i++)
		imgData.push_back(imageBuf[i]);
	Mat test(imgData, false);
	img = imdecode(test, CV_LOAD_IMAGE_COLOR);
	int x = 0;
	int y = 0;

	/// Convert it to gray
	cvtColor(img, src_gray, CV_BGR2GRAY);
	cvtColor(img, src_HSV, CV_BGR2HSV);
	/// Reduce the noise so we avoid false circle detection
	GaussianBlur(src_gray, src_gray, Size(9, 9), 2, 2);

	////////////엣지찾기//////////
	bool IsCircle = 0;
	bool IsEllipse = 0;
	findCircle(src_gray, img, x, y, IsCircle);
	findEllipse(src_gray, img, x, y, IsEllipse);


	///색찾기
	vector<Mat> channels;
	split(src_HSV, channels);
	uchar* data = channels[0].data;
	H = 2 * channels[0].at<uchar>(y, x);
	string color;
	if (H <= 30 || H >= 330)
	{
		color = "red";//"Red";
	}
	else if (30 <= H && H <= 90)
	{
		color = "yellow";//"Yellow";
	}
	else if (90 <= H && H <= 240)
	{
		color = "blue";//"Sky";
	}
	else if (240 <= H && H <= 330)
	{
		color = "pink";//"Pink";
	}
	else
		color = "";

	string shape;
	if (IsCircle == true)
	{
		if (IsEllipse == true)
			shape = "ellipse";//"타원";
		else
			shape = "circle";//"원";
	}
	else if (IsEllipse == true)
	{
		shape = "ellipse";//"타원";
	}
	else
		shape = "";//"알수없는 약품입니다.";

	waitKey(0);
	Pill pill;
	pill.setColor(color);
	pill.setShape(shape);
	pill.setDosageForm("");
	pill.setDivisionLine("");
	return pill;
}

void PillAnalyzer::findCircle(Mat src_gray, Mat src, int &x, int &y, bool &IsCircle)
{
	vector<Vec3f> circles;
	int edgeThresh = 1;
	int lowThreshold = 20;
	int const max_lowThreshold = 100;
	int ratio = 3;
	int kernel_size = 3;
	/// Apply the Hough Transform to find the circles
	HoughCircles(src_gray, circles, CV_HOUGH_GRADIENT, 1, src_gray.rows / 8, 30, 50, 0, 0);

	if (circles.empty() == false)
	{
		IsCircle = true;
		x = circles[0][0];
		y = circles[0][1];
	}	
}

void PillAnalyzer::findEllipse(Mat src_gray, Mat src, int &x, int &y, bool &IsEllipse)
{
	Mat threshold_output;
	Mat edge;
	vector<vector<Point> > contours;
	vector<Vec4i> hierarchy;
	int thresh = 73;
	int points = 50;
	int max_thresh = 255;
	RNG rng(12345);

	/// Detect edges     
	Canny(src_gray, edge, thresh, 3 * thresh, 3);
	edge.convertTo(threshold_output, CV_8U);
	/// Find contours
	findContours(threshold_output, contours, hierarchy, CV_RETR_TREE, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));

	/// Find the rotated rectangles and ellipses for each contour
	vector<RotatedRect> minRect(contours.size());
	vector<RotatedRect> minEllipse(contours.size());

	for (int i = 0; i < contours.size(); i++)
	{
		minRect[i] = minAreaRect(Mat(contours[i]));
		if (contours[i].size() > points)
		{
			minEllipse[i] = fitEllipse(Mat(contours[i]));
		}
	}

	if (minEllipse.empty() == false)
	{
		if (minEllipse[0].size.height >= 1.5*minEllipse[0].size.width || 1.5*minEllipse[0].size.height <= minEllipse[0].size.width)
			IsEllipse = true;
		x = minEllipse[0].center.x;
		y = minEllipse[0].center.y;
	}
	/*
	/// Draw contours + rotated rects + ellipses
	Mat drawing = Mat::zeros( threshold_output.size(), CV_8UC3 );
	for( int i = 0; i< contours.size(); i++ )
	{
	Scalar color = Scalar( rng.uniform(0, 255), rng.uniform(0,255), rng.uniform(0,255) );
	// contour
	//drawContours( src, contours, i, color, 1, 8, vector<Vec4i>(), 0, Point() );
	// ellipse
	ellipse( src, minEllipse[i], color, 2, 8 );
	// rotated rectangle
	//Point2f rect_points[4]; minRect[i].points( rect_points );
	//for( int j = 0; j < 4; j++ )
	//	line( drawing, rect_points[j], rect_points[(j+1)%4], color, 1, 8 );
	}

	/// Show in a window
	namedWindow( "Contours", CV_WINDOW_AUTOSIZE );
	imshow( "Contours", src );
	*/
}