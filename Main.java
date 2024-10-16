import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Введите исходную систему счисления (2-16): ");
            int base = scanner.nextInt();
            if (base < 2 || base > 16) {
                throw new IllegalArgumentException("Система счисления должна быть в диапазоне от 2 до 16");
            }

            System.out.println("Введите первое число: ");
            String num1Str = scanner.next();
            System.out.println("Введите второе число: ");
            String num2Str = scanner.next();

            System.out.println("Введите операцию (+, -, *, /): ");
            char operation = scanner.next().charAt(0);

            String num1Binary = toBinary(num1Str, base);
            String num2Binary = toBinary(num2Str, base);

            String resultBinary;
            switch (operation) {
                case '+':
                    resultBinary = addBinary(num1Binary, num2Binary);
                    break;
                case '-':
                    resultBinary = subtractBinary(num1Binary, num2Binary);
                    break;
                case '*':
                    resultBinary = multiplyBinary(num1Binary, num2Binary);
                    break;
                case '/':
                    if (num2Str.equals("0")) {
                        throw new IllegalArgumentException("Делить на 0 нельзя");
                    }
                    resultBinary = divideBinary(num1Binary, num2Binary);
                    break;
                default:
                    throw new IllegalArgumentException("Неподдерживаемая операция");
            }

            System.out.println("В двоичной системе: " + resultBinary);

            String baseResult = fromBinary(resultBinary, base);
            System.out.println("Результат в исходной системе счисления: " + baseResult);

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Предварительное преобразование в десятичную систему счисления
     * @param number
     * @param base
     * @return
     */
    public static String toBinary(String number, int base) {
        int decimal = 0;
        int len = number.length();

        for (int i = 0; i < len; i++) {
            char c = number.charAt(i);
            int value = Character.digit(c, base);
            if (value < 0 || value >= base) {
                throw new IllegalArgumentException("Недопустимый символ в числе: " + c);
            }
            decimal = decimal * base + value;
        }

        return decimalToBinary(decimal);
    }

    /**
     * Преобразование из десятичного в двоичное
     * @param decimal
     * @return
     */
    public static String decimalToBinary(int decimal) {
        StringBuilder binary = new StringBuilder();
        while (decimal > 0) {
            binary.insert(0, decimal % 2);
            decimal /= 2;
        }
        return !binary.isEmpty() ? binary.toString() : "0";
    }

    /**
     * Метод сложения
     * @param a
     * @param b
     * @return
     */
    public static String addBinary(String a, String b) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        int maxLength = Math.max(a.length(), b.length());

        a = padLeft(a, maxLength);
        b = padLeft(b, maxLength);

        for (int i = maxLength - 1; i >= 0; i--) {
            int bitA = a.charAt(i) - '0';
            int bitB = b.charAt(i) - '0';
            int sum = bitA + bitB + carry;

            result.insert(0, sum % 2);
            carry = sum / 2;
        }

        if (carry > 0) {
            result.insert(0, carry);
        }

        return result.toString();
    }

    /**
     * Метод для вычитания (используя дополнительный код)
     * @param a
     * @param b
     * @return
     */
    public static String subtractBinary(String a, String b) {
        String bComplement = twosComplement(b, a.length());
        return addBinary(a, bComplement).substring(1);
    }

    /**
     * Метода нахождения допоплнительного кода
     * @param binary
     * @param length
     * @return
     */
    public static String twosComplement(String binary, int length) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < binary.length(); i++) {
            result.append(binary.charAt(i) == '0' ? '1' : '0');
        }

        return padLeft(addBinary(result.toString(), "1"), length);
    }

    /**
     * Метод для умножения
     * @param a
     * @param b
     * @return
     */
    public static String multiplyBinary(String a, String b) {
        String result = "0";
        int length = b.length();

        for (int i = length - 1; i >= 0; i--) {
            if (b.charAt(i) == '1') {
                result = addBinary(result, a + "0".repeat(length - 1 - i));
            }
        }

        return result;
    }

    /**
     * Методаа для деления
     * @param dividend
     * @param divisor
     * @return
     */
    public static String divideBinary(String dividend, String divisor) {
        StringBuilder quotient = new StringBuilder();
        String remainder = "0";

        for (int i = 0; i < dividend.length(); i++) {
            remainder = addBinary(remainder + dividend.charAt(i), "0");

            if (compareBinary(remainder, divisor) >= 0) {
                quotient.append('1');
                remainder = subtractBinary(remainder, divisor);
            } else {
                quotient.append('0');
            }
        }

        return quotient.toString();
    }

    /**
     * Метод для сравнения двух двоичных чисел
     * @param a
     * @param b
     * @return
     */
    public static int compareBinary(String a, String b) {
        a = a.replaceFirst("^0+(?!$)", "");
        b = b.replaceFirst("^0+(?!$)", "");

        if (a.length() > b.length()) return 1;
        if (a.length() < b.length()) return -1;

        return a.compareTo(b);
    }

    /**
     * Метод для выравнивания строк
     * @param str
     * @param length
     * @return
     */
    public static String padLeft(String str, int length) {
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }

    /**
     * Преобразовние в исходную сс
     * @param binary
     * @param base
     * @return
     */
    public static String fromBinary(String binary, int base) {
        int decimal = binaryToDecimal(binary);

        StringBuilder result = new StringBuilder();
        while (decimal > 0) {
            int remainder = decimal % base;
            result.insert(0, Character.forDigit(remainder, base));
            decimal /= base;
        }
        return !result.isEmpty() ? result.toString() : "0";
    }

    /**
     * Преобразование двочиного числа в десятичное
     * @param binary
     * @return
     */
    public static int binaryToDecimal(String binary) {
        int decimal = 0;
        int len = binary.length();
        for (int i = 0; i < len; i++) {
            char bit = binary.charAt(len - 1 - i);
            if (bit == '1') {
                decimal += Math.pow(2, i);
            }
        }
        return decimal;
    }
}
