package currycoin;

public record TransactionInput(Hash prevTx, int index, Hash sig) {

}
