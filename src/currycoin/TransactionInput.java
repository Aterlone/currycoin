package currycoin;

import currycoin.script.Script;

public record TransactionInput(Hash prevTransaction, int index, Script lockingScript, Script unlockingScript) {

}
