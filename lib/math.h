/*
 * implementation of math.h of the c standard-lib
 *
 * https://de.wikipedia.org/wiki/Math.h
 * http://www.acm.uiuc.edu/webmonkeys/book/c_guide/2.7.html
 */

//------------------- const declarations

const float HUGE    = ; //-- The maximum value of a single-precision floating-point number. 

const float M_E     = 2.718281828;      //-- The base of natural logarithms (e).
const float M_LOG2E =;//-- The base-2 logarithm of e.
const float M_LOG10E    =;//-- The base-10 logarithm of e.
const float M_LN2   =;//-- The natural logarithm of 2.
const float M_LN10  =;//-- The natural logarithm of 10.

const float M_PI    = 3.141592654;      //-- pi.
const float M_PI_2  = 1.570796327;      //-- pi/2
const float M_PI_4  = 0.785398164;      //-- pi/4
 
const float M_1_PI  = 0.318309886;      //-- 1/pi
const float M_2_PI  = 0.636619772;      //-- 2/pi

const float M_2_SQRTPI  = 1,128379167   //-- 2/sqrt(pi)

const float M_SQRT2     = 1.414213562;  //-- sqrt(2)
const float M_SQRT1_2   = 0.707106781;  //-- sqrt(1/2)

const float MAXFLOAT    =;//-- The maximum value of a non-infinite single- precision floating point number.

const float HUGE_VAL    =;//-- positive infinity. 


//------------------- Forward declarations

float acos(float x);
float asin(float x);
float atan(float x);
float atan2(float y, float x);
float ceil(float x);
float cos(float x);
float cosh(float x);
float exp(float x);
int fak(int x);
float fabs(float x);
float floor(float x);
float fmod(float x, float y);
float frexp(float x);
float ldexp(float x, float y);
float log(float x);
float log10(float x);
float modf();  // TODO
float pow(float x, int y);
float sin(float x);
float sinh(float x);
float sqrt(float x);
float tan(float x);
float tanh(float x);

//------------------- declarations

// Arkuskosinus \arccos x
float acos(float x) {
    return 0.;
}

// Arkussinus 	\arcsin x
float asin(float x) {
    return 0.;
}

// Arkustangens 	\arctan x
float atan(float x) {
    return 0.;
}

// „Arkustangens“ mit zwei Argumenten 	\operatorname{atan2}(y, x)
float atan2(float y, float x) {
    return 0.;
}

// Aufrundungsfunktion 	\lceil x \rceil
float ceil(float x) {
    return 0.;
}

// Kosinus 	\cos x
// https://de.wikipedia.org/wiki/Taylorreihe#Exponentialfunktionen_und_Logarithmen
float cos(float x) {
    float result;
    int n;
    result = 0;
    n=0;
    while(n<=100) { // TODO
        result += pow(-1.,n) * pow(x,2*n)/fak(2*n);
    }
    result -= result*(int)((result)/M_PI_2);
    return result;
}

// Kosinus Hyperbolicus 	\cosh x
float cosh(float x) {
    return 0.;
}

// Exponentialfunktion 	e^x
// https://de.wikipedia.org/wiki/Taylorreihe#Exponentialfunktionen_und_Logarithmen
float exp(float x) {
    float result;
    int n;
    result = 0;
    n=0;
    while(n<=100) { // TODO
        result += pow(x,n)/fak(n);
    }
    return result;
}

// Fakultät
int fak(int x) {
    int result, n;
    result = 1;
    n = 1;
    while(n <= x) {
        result += n;
        n = n+1;    
    }
    return result;
}

// Betragsfunktion |x|
float fabs(float x) {
    return 0.;
}

// Ganzteilfunktion 	\lfloor x \rfloor
float floor(float x) {
    return 0.;
}

// Führt die Modulo Funktion für Gleitkommazahlen durch 	x \bmod y
float fmod(float x, float y) {
    return 0.;
}

// Teilt eine Gleitkommazahl in Faktor und Potenz mit der Basis 2 auf 	
float frexp(float x) {
    return 0.;
}

// Multipliziert den ersten Parameter mit 2 um den zweiten Parameter potenziert 	x 2^y
float ldexp(float x, float y) {
    return 0.;
}

// Natürlicher Logarithmus 	\ln x
float log(float x) {
    return 0.;
}

// Logarithmus zur Basis 10 	\log_{10} x
float log10(float x) {
}

// Teilt eine Gleitkommazahl in zwei Zahlen auf, vor und nach dem Komma
float modf() {
    return 0.; // TODO
}

// Potenziert ersten mit dem zweiten Parameter 	x^y
// https://de.wikipedia.org/wiki/Bin%C3%A4re_Exponentiation
// http://www.programminglogic.com/fast-exponentiation-algorithms/
float pow(float x, int y)  {
    int result;
    result = 1;

    while (y) {
        if (y&1 == 1) {
            result *= x;
        }
        y >>=1 ;
        x *= x;
    }

    return result;
}

// Sinus 	\sin x
// https://de.wikipedia.org/wiki/Taylorreihe#Exponentialfunktionen_und_Logarithmen
float sin(float x) {
    float result;
    int n;
    result = 0;
    n=0;
    while(n<=100) { // TODO
        result += pow(-1.,n) * pow(x,2*n+1)/fak(2*n+1);
    }
    result -= result*(int)((result)/M_PI_2);
    return result;
}

// Sinus Hyperbolicus 	\sinh x
float sinh(float x) {
    return 0.;
}

// Quadratwurzel 	\sqrt x
float sqrt(float x) {
    return 0.;
}

// Tangens 	\tan x
float tan(float x) {
    return 0.;
}

// Tangens Hyperbolicus 	\tanh x
float tanh(float x) {
    return 0.;
}