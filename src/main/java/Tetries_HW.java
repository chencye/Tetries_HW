import com.github.chencye.game.tetries.Ai;
import com.github.chencye.game.tetries.Block;
import com.github.chencye.game.tetries.Grid;

import java.util.Arrays;

public class Tetries_HW extends JavaPlayer {

    /**
     * 网格总行数
     **/
    static final int TOTAL_ROW = 20;
    /**
     * 风格总列数
     **/
    static final int TOTAL_COLUMN = 10;

    /**
     * 方块总类型数
     **/
    static final int TOTAL_TYPE = 7;

    /**
     * 方块类型：用于存储前TOTAL_TYPE次的方块，以判断方块是否以固定顺序出现
     **/
    static int[] types = new int[TOTAL_TYPE];

    static final int REPEAT_LENGTH = 2;
    /**
     * 重复出现的方块：TOTAL_TYPE次后，重复出现的方块，用来判断方块是否以固定顺序出现
     **/
    static int[] repeatTypes = new int[REPEAT_LENGTH];

    /**
     * 根据这么多次出现的方块，进行判断是否以固定顺序出现
     **/
    static final int CHECK_REPEAT_LENGTH = TOTAL_TYPE + REPEAT_LENGTH;
    /**
     * 判断次数，用于累计CHECK_REPEAT_LENGTH
     **/
    static int checkCount;

    /**
     * 是否以固定顺序重复出现
     **/
    static boolean isRepeat = false;
    /**
     * 当在最前面的方块就有重复出现时，就不需要后续的检查了，判断为随机出现
     **/
    static boolean breakCheck = false;

    /**
     * 当方块以固定顺序出现时，取未来方块数量，以判定当前方块的旋转及下落位置
     **/
    static int workingBlockLength = 5;

    static Ai ai = new Ai();

    public void play() {
        int currBlock = api_getCurrBlock();
        checkRepeat(currBlock);
        int[] workingBlocks = isRepeat ? getWorkingBlocks(currBlock, workingBlockLength) : new int[]{currBlock, api_getNextBlock()};
        System.out.println("types: " + Arrays.toString(types));
        System.out.println("repeatTypes: " + Arrays.toString(repeatTypes));
        System.out.println("workingBlocks: " + Arrays.toString(workingBlocks));
        System.out.println("-------isRepeat: " + isRepeat);
        Block block = ai.getBest(getGrid(TOTAL_ROW, TOTAL_COLUMN), workingBlocks);
        api_addStepMethod(0, 0);
    }

    private int[] getWorkingBlocks(int currBlock, int length) {
        int index = Arrays.binarySearch(types, currBlock);
        int[] workingBlocks = new int[length];
        for (int i = 0; i < length; i++) {
            int typesindex = index + i;
            typesindex = typesindex >= types.length ? typesindex - types.length : typesindex;
            workingBlocks[i] = types[typesindex];
        }
        return workingBlocks;
    }

    /**
     * 判断方块是否以固定顺序重复出现
     *
     * @param currBlock
     */
    private void checkRepeat(int currBlock) {
        // 已判定重复，或标记中断检查，则直接返回
        if (isRepeat || breakCheck) {
            return;
        }
        // 如果轮次，大于10次，直接返回
        if (checkCount > CHECK_REPEAT_LENGTH) {
            return;
        }
        // 达到10轮，判断types与repeatTypes是否重合
        if (checkCount == CHECK_REPEAT_LENGTH) {
            isRepeat = true;
            for (int i = 0; i < CHECK_REPEAT_LENGTH - TOTAL_TYPE; i++) {
                if (types[i] != repeatTypes[i]) {
                    isRepeat = false;
                    break;
                }
            }
            return;
        }
        // 小于7次，存入types中
        if (checkCount < TOTAL_TYPE) {
            // 小于7次时，出现了重复方块，判定不是固定顺序出现，并标记中断检查
            if (Arrays.binarySearch(types, currBlock) > 0) {
                isRepeat = false;
                breakCheck = true;
            }
            types[checkCount++] = currBlock;
            return;
        }
        // 大于7次，小于10次，存入repeatTypes中
        repeatTypes[checkCount++ - TOTAL_TYPE] = currBlock;
    }

    /**
     * 调用api_getMatrixInfo查询当前整个网格状态
     *
     * @return
     */
    private Grid getGrid(int totalRow, int totalColumn) {
        int[][] cells = new int[totalRow][totalColumn];
        for (int row = 0; row < totalRow; row++) {
            for (int col = 0; col < totalColumn; col++) {
                cells[row][col] = api_getMatrixInfo(row, col) ? 1 : 0;
            }
        }
        return new Grid(totalRow, totalColumn, cells);
    }
}
