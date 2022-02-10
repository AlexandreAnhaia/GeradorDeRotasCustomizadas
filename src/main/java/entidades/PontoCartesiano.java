package entidades;

public class PontoCartesiano {

    private static long idCount = 0;

    public final long id;
    public final double x; //Posição relativa a base(HOME) unidade: metros;
    public final double y; //Posição relativa a base(HOME) unidade: metros
    public final double z; //Altura em metros.

    public PontoCartesiano(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = idCount++;
    }

    @Override
    public String toString() {
        return "" + id;
    }

    /**
     * Calcula a distância de um ponto até o outro em metros
     * @param other
     * @return
     */
    public PontoCartesiano minus(PontoCartesiano other) {
        double x = this.x - other.x;
        double y = this.y - other.y;
        double z = this.z - other.z;
        return new PontoCartesiano(x, y, z);
    }

    /**
     * Calcula a soma de dois pontos cartesianos em metros
     * @param other
     * @return
     */
    public PontoCartesiano sum(PontoCartesiano other) {
        double x = this.x + other.x;
        double y = this.y + other.y;
        double z = this.z + other.z;
        return new PontoCartesiano(x, y, z);
    }

    /**
     * Divide um vetor grande em partes pequenas, para a formação da rota. P3= P1 / number
     * @param number
     * @return
     */
    public PontoCartesiano divide(double number) {
        double x = this.x / number;
        double y = this.y / number;
        double z = this.z / number;
        return new PontoCartesiano(x, y, z);
    }

    /**
     * Adiciona no ponto atual com um outro ponto multiplicado por um coeficiente. P3 = P1 + coef * P2 P3 é o resultado a ser retornado; P1 é o
     * objeto atual ao qual foi chamado a função; P2 é o objeto passado como parâmetro.
     * @param coef
     * @param other
     * @return
     */
    public PontoCartesiano sumProd(double coef, PontoCartesiano other) {
        double x = this.x + coef * other.x;
        double y = this.y + coef * other.y;
        double z = this.z + coef * other.z;
        return new PontoCartesiano(x, y, z);
    }

    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public PontoCartesiano vectorialProd(PontoCartesiano vb) {
        return new PontoCartesiano(
                this.y*vb.z - this.z*vb.y,  //a2b3 − a3b2,
                this.z*vb.x - this.x*vb.z,  //a3b1 − a1b3,
                this.x*vb.y - this.y*vb.x   //a1b2 − a2b1
        );
    }
}
