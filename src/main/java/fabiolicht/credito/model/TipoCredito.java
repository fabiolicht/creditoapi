package fabiolicht.credito.model;

public enum TipoCredito {
    PRINCIPAL("Principal"),
    COMPLEMENTAR("Complementar"),
    ADICIONAL("Adicional"),
    RETIFICACAO("Retificação"),
    CANCELAMENTO("Cancelamento");

    private final String descricao;

    TipoCredito(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
