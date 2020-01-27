package unicauca.front.end.service;

import java.util.HashMap;

/**
 *
 * @author julia
 */
public class ElemMatch {

    private HashMap<String, Object> $elemMatch;

    public ElemMatch(HashMap<String, Object> $elemMatch) {
        this.$elemMatch=$elemMatch;
    }

    public HashMap<String, Object> get$elemMatch() {
        return $elemMatch;
    }

    public void set$elemMatch(HashMap<String, Object> $elemMatch) {
        this.$elemMatch = $elemMatch;
    }


}
