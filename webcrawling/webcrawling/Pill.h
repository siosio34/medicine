#pragma once
#include <iostream>
using namespace std;

class Pill {
public:
	Pill() {};
	string getName() { return name; }
	string getColor() { return color; }
	string getShape() { return shape; }
	string getDosageForm() { return dosageForm; }
	string getDivisionLine() { return divisionLine; }
	void setName(string _name) { name = _name; }
	void setColor(string _color) { color = _color; }
	void setShape(string _shape) { shape = _shape; }
	void setDosageForm(string _dosageForm) { dosageForm = _dosageForm; }
	void setDivisionLine(string _divisionLine) { divisionLine = _divisionLine; }

private:
	string name;
	string color;
	string shape;
	string dosageForm;
	string divisionLine;
};