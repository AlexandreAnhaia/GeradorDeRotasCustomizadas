package entidades;

import static Controladores.Controlador.converteParaCartesiano;

public class AreaCartesiana {

    public final AreaGeografica areaGeografica;
    public final PontoCartesiano pontoInicial;
    public final PontoCartesiano[] preMissao;
    public final PontoCartesiano ponto1;
    public final PontoCartesiano ponto2;
    public final PontoCartesiano ponto3;
    public final PontoCartesiano ponto4;
    public final PontoCartesiano[] posMissao;

    public final double hipotenusa;
    public final double comprimentoBase;

    public AreaCartesiana(AreaGeografica area) {
        this.areaGeografica = area;
        this.pontoInicial = converteParaCartesiano(area.pontoInicial, area.pontoInicial);
        this.ponto1 = converteParaCartesiano(area.ponto1, area.pontoInicial);
        this.ponto2 = converteParaCartesiano(area.ponto2, area.pontoInicial);
        this.ponto3 = converteParaCartesiano(area.ponto3, area.pontoInicial);
        this.ponto4 = converteParaCartesiano(area.ponto4, area.pontoInicial);
        this.preMissao = area.preMissao.stream().map(gp -> converteParaCartesiano(gp, area.pontoInicial)).toArray(PontoCartesiano[]::new);
        this.posMissao = area.posMissao.stream().map(gp -> converteParaCartesiano(gp, area.pontoInicial)).toArray(PontoCartesiano[]::new);
        this.hipotenusa = calculaHipotenusa(ponto1, ponto2, ponto3, ponto4);
        this.comprimentoBase = calculaComprimentoBase(ponto1, ponto2, ponto3, ponto4);
    }

    /**
     * Calcula o tamanho da parte frontal da área de interesse a ser mapeada.
     * @param p1 ponto 1 da área de interesse
     * @param p2 ponto 2 da área de interesse
     * @param p3 ponto 3 da área de interesse
     * @param p4 ponto 4 da área de interesse
     * @return
     */
    private static double calculaHipotenusa(PontoCartesiano p1, PontoCartesiano p2, PontoCartesiano p3, PontoCartesiano p4) {
        PontoCartesiano h1 = p4.minus(p1);
        PontoCartesiano h2 = p3.minus(p2);
        return Math.max(h1.norm(), h2.norm());
    }

    /**
     * Calcula o comprimento da base, com base nos quatros pontos da área a ser mapeada.
     * @param p1 ponto 1 da área de interesse
     * @param p2 ponto 2 da área de interesse
     * @param p3 ponto 3 da área de interesse
     * @param p4 ponto 4 da área de interesse
     * @return
     */
    private double calculaComprimentoBase(PontoCartesiano p1, PontoCartesiano p2, PontoCartesiano p3, PontoCartesiano p4) {
        PontoCartesiano h1 = p4.minus(p3);
        PontoCartesiano h2 = p1.minus(p2);
        return Math.max(h1.norm(), h2.norm());
    }
}
