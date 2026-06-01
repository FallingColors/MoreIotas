package ram.talia.moreiotas.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.utils.HexUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jblas.DoubleMatrix;
import org.ejml.simple.SimpleMatrix;
import org.jetbrains.annotations.NotNull;
import ram.talia.moreiotas.api.mod.MoreIotasConfig;
import ram.talia.moreiotas.api.matrices.MatrixConverter;
import ram.talia.moreiotas.common.lib.hex.MoreIotasIotaTypes;

public class MatrixIota extends Iota {
    public MatrixIota(@NotNull SimpleMatrix matrix) throws MishapInvalidIota {
        super(MoreIotasIotaTypes.MATRIX, matrix);
        if (matrix.getNumRows() > MoreIotasConfig.getServer().getMaxMatrixSize() || matrix.getNumCols() > MoreIotasConfig.getServer().getMaxMatrixSize())
            throw MishapInvalidIota.of(this,
                    0,
                    "matrix.max_size",
                    MoreIotasConfig.getServer().getMaxMatrixSize(),
                    matrix.getNumRows(),
                    matrix.getNumCols());
    }

    /**
     * @deprecated Use of {@link DoubleMatrix} (and JBLAS in general) is deprecated, change to {@link SimpleMatrix} instead
     */
    @Deprecated(since = "0.1.2")
    public MatrixIota(@NotNull DoubleMatrix jblasMatrix) throws MishapInvalidIota {
        this(MatrixConverter.jblasToEjml(jblasMatrix));
    }

    public SimpleMatrix getSimpleMatrix() {
        return (SimpleMatrix) this.payload;
    }

    /**
     * @deprecated Use of {@link DoubleMatrix} (and JBLAS in general) is deprecated, change to {@link SimpleMatrix} instead
     */
    @Deprecated(since = "0.1.2")
    public DoubleMatrix getMatrix() { return MatrixConverter.ejmlToJblas((SimpleMatrix) this.payload); }

    @Override
    protected boolean toleratesOther(Iota that) {
        if (!(that instanceof MatrixIota matrix)) {
            return false;
        }
        return matrix.getSimpleMatrix().isIdentical(this.getSimpleMatrix(), 0.001);
    }

    @Override
    public boolean isTruthy() {
        // is true if it has entries, and at least one has abs(entry)>0
        return this.getSimpleMatrix().elementMaxAbs() > DoubleIota.TOLERANCE;
    }

    @Override
    public @NotNull Tag serialize() {
        var tag = new CompoundTag();

        var mat = this.getSimpleMatrix();

        tag.putInt(TAG_ROWS, mat.getNumRows());
        tag.putInt(TAG_COLS, mat.getNumCols());

        var list = new ListTag();

        for (int i = 0; i < mat.getNumRows(); i++) {
            var curList = new ListTag();
            for (int j = 0; j < mat.getNumCols(); j++) {
                curList.add(DoubleTag.valueOf(mat.get(i, j)));
            }
            list.add(curList);
        }

        tag.put(TAG_MAT, list);
        return tag;
    }

    public static IotaType<MatrixIota> TYPE = new IotaType<>() {
        @Override
        public MatrixIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            var ctag = HexUtils.downcast(tag, CompoundTag.TYPE);
            try {
                return new MatrixIota(deserialise(ctag));
            } catch (MishapInvalidIota e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public Component display(Tag tag) {
            if (!(tag instanceof CompoundTag ctag)) {
                return Component.translatable("hexcasting.spelldata.unknown");
            }

            SimpleMatrix mat;
            try {
                mat = deserialise(ctag);
            } catch (IllegalArgumentException e) {
                return Component.translatable("hexcasting.spelldata.unknown");
            }

            var out = Component.empty();

            out.append(String.format("(%d, %d)", mat.getNumRows(), mat.getNumCols()));
            if (!(mat.getNumCols() == 0 && mat.getNumRows() == 0))
                out.append(" | ");

            for (int r = 0; r < mat.getNumRows(); r++) {
                for (int c = 0; c < mat.getNumCols(); c++) {
                    out.append(Component.literal(String.format("%.2f", mat.get(r,c))).withStyle(ChatFormatting.GREEN));
                    if (c < mat.getNumCols() - 1) {
                        out.append(", ");
                    }
                }
                if (r < mat.getNumRows() - 1) {
                    out.append("; ");
                }
            }

            return Component.translatable("hexcasting.tooltip.list_contents", out).withStyle(ChatFormatting.AQUA);
        }

        private SimpleMatrix deserialise(CompoundTag ctag) throws IllegalArgumentException {

            if (!ctag.contains(TAG_ROWS) || !ctag.contains(TAG_COLS) || !ctag.contains(TAG_MAT))
                throw new IllegalArgumentException("expected tags \"rows\": int, \"cols\": int, and \"mat\": list(list(double))");

            int rows = ctag.getInt(TAG_ROWS);
            int cols = ctag.getInt(TAG_COLS);

            var mat = new SimpleMatrix(rows, cols);

            var list = ctag.getList(TAG_MAT, Tag.TAG_LIST);

            for (int i = 0; i < rows; i++) {
                var curList = list.getList(i);
                for (int j = 0; j < cols; j++) {
                    mat.set(i, j, curList.getDouble(j));
                }
            }

            return mat;
        }

        @Override
        public int color() {
            return 0xff_55ffff;
        }
    };

    private static final String TAG_ROWS = "rows";
    private static final String TAG_COLS = "cols";
    private static final String TAG_MAT = "mat";
}
