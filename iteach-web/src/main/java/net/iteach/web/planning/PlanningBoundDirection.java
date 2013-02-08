package net.iteach.web.planning;

public enum PlanningBoundDirection {

    MINUS {
        @Override
        public int change(int value) {
            return value - 1;
        }
    },

    PLUS {
        @Override
        public int change(int value) {
            return value + 1;
        }
    };

    public abstract int change(int value);

}
