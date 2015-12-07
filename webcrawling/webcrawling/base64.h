#include <string>

std::string base64_encode(unsigned char const* , unsigned int len);
unsigned char* base64_decode(std::string const& s, int& size);

