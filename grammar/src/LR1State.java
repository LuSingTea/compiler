import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class LR1State {

    // 代表了一个LR(1)项目集所有的项目
    private LinkedHashSet<LR1Item> items;
    // 状态集和所有LR(1)项目集的映射
    private HashMap<String,LR1State> transition;

    // 传入L1项目集和语法,计算闭包
    public LR1State(Grammar grammar,HashSet<LR1Item> coreItems){
        items = new LinkedHashSet<>(coreItems);
        transition = new HashMap<>();
        closure(grammar);
    }

    private void closure(Grammar grammar) {
        boolean changeFlag = false;
        do {
            changeFlag = false;
            for(LR1Item item : items){
                if(item.getDotPointer() != item.getRightSide().length && grammar.isVariable(item.getCurrent())){
                    HashSet<String> lookahead = new HashSet<>();
                    if(item.getDotPointer() == item.getRightSide().length - 1){
                        lookahead.addAll(item.getLookahead());
                    }else{
                        HashSet<String> firstSet = grammar.computeFirst(item.getRightSide(),item.getDotPointer()+1);
                        if(firstSet.contains("epsilon")){
                            firstSet.remove("epsilon");
                            firstSet.addAll(item.getLookahead());
                        }
                        lookahead.addAll(firstSet);
                    }
                    HashSet<Rule> rules = grammar.getRuledByLeftVariable(item.getCurrent());
                    for(Rule rule : rules){
                        String[] rhs = rule.getRightSide();
                        int finished = 0;
                        if (rhs.length == 1 && rhs[0].equals("epsilon")) {
                            finished = 1;
                        }
                        HashSet<String> newLA = new HashSet<String>(lookahead);
                        LR1Item newItem = new LR1Item(rule.getLeftSide(),rhs,finished,newLA);
                        // merge lookaheads with existing item
                        boolean found = false;
                        for (LR1Item existingItem : items) {
                            if (newItem.equalLR0(existingItem)) {
                                HashSet<String> existLA = existingItem.getLookahead();
                                if (!existLA.containsAll(newLA)) {
                                    // changing the lookahead will change the hash code
                                    // of the item, which means it must be re-added.
                                    items.remove(existingItem);
                                    existLA.addAll(newLA);
                                    items.add(existingItem);
                                    changeFlag = true;
                                }
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            items.add(newItem);
                            changeFlag = true;
                        }
                    }
                    if (changeFlag) {
                        break;
                    }
                }
            }
        } while (changeFlag);

    }

    public HashMap<String, LR1State> getTransition() {
        return transition;
    }

    public LinkedHashSet<LR1Item> getItems() {
        return items;
    }

    @Override
    public String toString() {
        String s = "";
        for(LR1Item item:items){
            s += item + "\n";
        }
        return s;
    }
}
