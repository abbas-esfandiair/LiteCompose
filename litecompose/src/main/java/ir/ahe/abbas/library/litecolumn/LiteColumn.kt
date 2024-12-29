package ir.ahe.abbas.library.litecolumn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxOfOrNull

@Composable
fun LiteColumn(
    modifier: Modifier = Modifier,
    spaceBetweenItem: Dp = 0.dp,
    horizontalAlignment: HorizontalAlignment = HorizontalAlignment.Start,
    content: @Composable LiteColumnScope.() -> Unit,
) {
    Layout(
        content = { LiteColumnScopeInstance.content() },
        modifier = modifier,
        measurePolicy = rememberColumnMeasurePolicy(spaceBetweenItem, horizontalAlignment)
    )
}

@NonRestartableComposable
@Composable
private fun rememberColumnMeasurePolicy(
    spaceBetweenItem: Dp,
    alignment: HorizontalAlignment
) = remember(spaceBetweenItem, alignment) {
    LiteColumnMeasurePolicy(spaceBetweenItem, alignment)
}

@Immutable
private class LiteColumnMeasurePolicy(
    private val space: Dp,
    private val parentAlignment: HorizontalAlignment
) : MeasurePolicy {

    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        val alignments = arrayOfNulls<LiteColumnChildDataNode>(measurables.size)

        val spaceAsPixel = space.toPx().toInt()

        val childConstraints = constraints.copy(
            minWidth = 0,
            maxWidth = if (constraints.hasBoundedWidth) constraints.maxWidth else Constraints.Infinity,
            minHeight = 0,
            maxHeight = if (constraints.hasBoundedHeight) constraints.maxHeight else Constraints.Infinity
        )

        val placeables = Array(measurables.size) { index ->
            alignments[index] = measurables[index].liteColumnChildDataNode
            measurables[index].measure(childConstraints)
        }

        val totalWidth = placeables.maxOfOrNull { it.width } ?: 0
        val constrainedWidth = constraints.constrainWidth(totalWidth)

        val totalHeight =
            placeables.sumOf { it.height } + (placeables.size - 1).coerceAtLeast(0) * spaceAsPixel

        val constrainedHeight = constraints.constrainHeight(totalHeight)

        var yPosition = 0

        return layout(constrainedWidth, constrainedHeight) {
            for (index in placeables.indices) {
                val alignment = alignments.getOrNull(index)
                val placeable = placeables.getOrNull(index) ?: continue

                val targetAlignment = alignment?.alignment ?: parentAlignment

                val xPosition = when (targetAlignment) {
                    HorizontalAlignment.Start -> 0
                    HorizontalAlignment.Center -> (constrainedWidth - placeable.width) / 2
                    HorizontalAlignment.End -> constrainedWidth - placeable.width
                }

                placeable.placeRelative(x = xPosition, y = yPosition)
                yPosition += placeable.height + spaceAsPixel
            }
        }
    }


    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        return measurables.fastMaxOfOrNull { maxIntrinsicWidthLambda(it, height) } ?: 0
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        return measurables.fastMaxOfOrNull { minIntrinsicWidthLambda(it, height) } ?: 0
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int
    ): Int {
        val childHeights = measurables.fastMap { maxIntrinsicHeightLambda(it, width) }
        val totalSpace = (childHeights.size - 1).coerceAtLeast(0) * space.roundToPx()

        return childHeights.sum() + totalSpace
    }


    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int
    ): Int {
        val childHeights = measurables.fastMap { minIntrinsicHeightLambda(it, width) }
        val totalSpace = (childHeights.size - 1).coerceAtLeast(0) * space.roundToPx()

        return childHeights.sum() + totalSpace
    }


}

private val maxIntrinsicWidthLambda: IntrinsicMeasurable.(Int) -> Int = { maxIntrinsicWidth(it) }
private val minIntrinsicWidthLambda: IntrinsicMeasurable.(Int) -> Int = { minIntrinsicWidth(it) }
private val maxIntrinsicHeightLambda: IntrinsicMeasurable.(Int) -> Int = { maxIntrinsicHeight(it) }
private val minIntrinsicHeightLambda: IntrinsicMeasurable.(Int) -> Int = { minIntrinsicHeight(it) }