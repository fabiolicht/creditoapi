package fabiolicht.credito.model;

public enum StatusCredito {
    ATIVO("Ativo"),
    INATIVO("Inativo"),
    PENDENTE("Pendente"),
    PROCESSANDO("Processando"),
    ERRO("Erro");

    private final String descricao;

    StatusCredito(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
