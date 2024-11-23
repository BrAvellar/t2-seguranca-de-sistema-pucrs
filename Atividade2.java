import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Atividade2 {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        // Recebendo a chave de sessão (128 bits) em hexadecimal
        System.out.println("Digite a chave de sessão (128 bits em hexadecimal):");
        String sHex = scanner.nextLine();
        byte[] chaveSessao = hexStringToByteArray(sHex);

        // Recebendo a mensagem cifrada do professor em hexadecimal
        System.out.println("\nDigite a mensagem cifrada recebida:");
        String mensagemHex = scanner.nextLine();
        byte[] mensagemBytes = hexStringToByteArray(mensagemHex);

        // Separando o IV (128 bits) e a mensagem
        byte[] ivRecebido = Arrays.copyOfRange(mensagemBytes, 0, 16);
        byte[] mensagemCifrada = Arrays.copyOfRange(mensagemBytes, 16, mensagemBytes.length);

        // Decifrando a mensagem
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec chave = new SecretKeySpec(chaveSessao, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivRecebido);
        cipher.init(Cipher.DECRYPT_MODE, chave, ivSpec);
        byte[] mensagemDecifradaBytes = cipher.doFinal(mensagemCifrada);

        String mensagemDecifrada = new String(mensagemDecifradaBytes, StandardCharsets.UTF_8);
        System.out.println("\nMensagem decifrada:");
        System.out.println(mensagemDecifrada);

        scanner.close();

        // Invertendo os caracteres da mensagem
        String mensagemInvertida = new StringBuilder(mensagemDecifrada).reverse().toString();
        System.out.println("\nMensagem invertida:");
        System.out.println(mensagemInvertida);

        // Cifrando a mensagem invertida com novo IV aleatório
        SecureRandom random = new SecureRandom();
        byte[] novoIV = new byte[16];
        random.nextBytes(novoIV);

        cipher.init(Cipher.ENCRYPT_MODE, chave, new IvParameterSpec(novoIV));
        byte[] mensagemInvertidaBytes = mensagemInvertida.getBytes(StandardCharsets.UTF_8);
        byte[] mensagemCifradaResposta = cipher.doFinal(mensagemInvertidaBytes);

        // Concatenando o novo IV e a mensagem cifrada
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(novoIV);
        outputStream.write(mensagemCifradaResposta);
        byte[] mensagemParaEnviar = outputStream.toByteArray();

        // Exibindo a mensagem a ser enviada em hexadecimal
        System.out.println("\nMensagem a ser enviada:");
        System.out.println(bytesToHex(mensagemParaEnviar)); 
    }

    // Métodos auxiliares
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        // Verificar se a string tem número par de caracteres
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Comprimento inválido da string hexadecimal.");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}