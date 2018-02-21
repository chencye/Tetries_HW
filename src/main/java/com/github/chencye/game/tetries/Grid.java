package com.github.chencye.game.tetries;

import java.util.Arrays;

public class Grid {

    int totalRow;
    int totalColumn;

    int[][] cells;

    public Grid(int totalRow, int totalColumn, int[][] cells) {
        this.totalRow = totalRow;
        this.totalColumn = totalColumn;
        this.cells = cells;
    }

    @Override
    public String toString() {
        return "Grid{" +
                "totalRow=" + totalRow +
                ", totalColumn=" + totalColumn +
                ", cells=" + Arrays.deepToString(cells) +
                '}';
    }
}
