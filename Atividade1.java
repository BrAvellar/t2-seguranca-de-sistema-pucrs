import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Scanner;

public class Atividade1 {
    public static void main(String[] args) throws Exception {
        // Valores de p e g fornecidos
        String pHex = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C69A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C013ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD7098488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708DF1FB2BC2E4A4371";

        String gHex = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507FD6406CFF14266D31266FEA1E5C41564B777E690F5504F213160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28AD662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24855E6EEB22B3B2E5";

        BigInteger p = new BigInteger(pHex, 16);
        BigInteger g = new BigInteger(gHex, 16);

        // Validando valores de p e g
        if (p.compareTo(BigInteger.ZERO) <= 0 || !p.isProbablePrime(100)) {
            throw new IllegalArgumentException("O valor de 'p' não é um número primo válido.");
        }
        if (g.compareTo(BigInteger.ZERO) <= 0 || g.compareTo(p) >= 0) {
            throw new IllegalArgumentException("O valor de 'g' não está dentro do intervalo válido (0 < g < p).");
        }

        // Gerando 'a' com pelo menos 30 dígitos decimais E menor que 'p'
        // SecureRandom random = new SecureRandom();
        // BigInteger a;
        // do {
        //     a = new BigInteger(p.bitLength() - 1, random); // Número menor que 'p'
        // } while (a.compareTo(BigInteger.TEN.pow(29)) < 0 && a.compareTo(p) >= 0);

        // Valor de 'a' enviado para professor
        BigInteger a = new BigInteger("3dc476cd2f28ff0e7e8080bb98437120558df01b41056cd7829cc38bba92b8cababaa0347280b2217f5fda59c976de0484fdc61d82c9daf9aa55e162115935a07ccf3c21c54e593eca2e85e21a039e8e8011a771066c018eda3e23ee0484b86555e394bb2e20e5e5d9fc36dd6267ab731b2798300fc096130178a41af4a91cfa", 16);

        // Validando 'a'
        if (a.compareTo(BigInteger.ZERO) <= 0 || a.compareTo(p) >= 0) {
            throw new IllegalArgumentException("O valor de 'a' não é válido.");
        }

        System.out.println("Valor de 'a' em decimal:");
        System.out.println(a.toString(16));

        // Calculando A = g^a mod p
        BigInteger A = g.modPow(a, p);
        System.out.println("\nValor de 'A' em hexadecimal:");
        System.out.println(A.toString(16));

        // Recebendo 'B' do professor
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nDigite o valor de 'B' recebido:");
        String bHex = scanner.nextLine();
        BigInteger B = new BigInteger(bHex, 16);

        // Calculando V = B^a mod p
        BigInteger V = B.modPow(a, p);

        // Gerando SHA-256 de V
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(V.toByteArray());

        // Obtendo os 16 bytes (128 bits) menos significativos do hash
        byte[] sBytes = new byte[16];
        System.arraycopy(hash, hash.length - 16, sBytes, 0, 16);
        System.out.println("S (chave de sessão): " + bytesToHex(sBytes));

        scanner.close();
    }

    // Método auxiliar para converter bytes em hexadecimal
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
