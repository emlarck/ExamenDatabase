package Utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Validacion {
    public static int Scanner_enteros(Scanner scan , String mensaje) {
        int numero = 0;
        boolean esNumeroValido = false;

        while (!esNumeroValido) {
            try {
                System.out.print(mensaje);
                numero = scan.nextInt();
                scan.nextLine();

                esNumeroValido = true;
            } catch (InputMismatchException e) {
                System.out.println("¡Error! Entrada inválida. Por favor, ingresa solo caracteres numéricos.");
                scan.nextLine();
            }
        }
        return numero;
    }

    public static String scan_string_vacio(Scanner sc) {
        String texto = "";
        boolean textoValido = false;

        while (!textoValido) {
            texto = sc.nextLine();

            if (texto == null || texto.trim().isEmpty()) {
                System.out.println("Este campo no puede estar vacio");
            } else {
                textoValido = true;
            }
        }

        return texto.trim();
    }

    public static double Scanner_doubles(Scanner leer, String mensaje) {
        double numero = 0;
        boolean valido = false;

        while (!valido) {
            System.out.print(mensaje);
            try {

                String entrada = leer.nextLine();
                numero = Double.parseDouble(entrada);
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor, ingrese un número decimal válido (ej. 150.50).");
            }
        }
        return numero;
    }




    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numero = -1;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print("Introduce un número entero y positivo: ");

            if (scanner.hasNextInt()) {
                numero = scanner.nextInt();


                if (numero >=0) {
                    entradaValida = true;
                } else {
                    System.out.println("Error: El número debe ser mayor a 0.");
                }
            } else {
                System.out.println("Error: Entrada inválida. Debes ingresar un número entero.");
                scanner.next();
            }
        }

        System.out.println("¡Número válido ingresado! Tu número es: " + numero);
        scanner.close();
    }


}








