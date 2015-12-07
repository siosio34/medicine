#pragma once
#include "Pill.h"
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/objdetect/objdetect.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <fstream>
using namespace cv;

class PillAnalyzer {
public:
	PillAnalyzer();
	Pill analyzePill(unsigned char* imageBuf, int size);
	void findCircle(Mat src_gray, Mat src, int &x, int &y, bool &IsCircle);
	void findEllipse(Mat src_gray, Mat src, int &x, int &y, bool &IsEllipse);
};