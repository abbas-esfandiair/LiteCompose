package ir.ahe.abbas.library.literow

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxOfOrNull

@Composable
fun LiteRow(
    modifier: Modifier = Modifier,
    spaceBetweenItem: Dp = 0.dp,
    verticalAlignment: VerticalAlignment = VerticalAlignment.Top,
    horizontalAlignment: HorizontalAlignment = HorizontalAlignment.Start,
    content: @Composable LiteRowScope.() -> Unit,
) {
    Layout(
        content = { LiteRowScopeInstance.content() },
        modifier = modifier,
        measurePolicy = rememberLiteRowMeasurePolicy(
            spaceBetweenItem,
            verticalAlignment,
            horizontalAlignment
        )
    )
}

@NonRestartableComposable
@Composable
private fun rememberLiteRowMeasurePolicy(
    spaceBetweenItem: Dp,
    verticalAlignment: VerticalAlignment,
    horizontalAlignment: HorizontalAlignment
) = remember(spaceBetweenItem, verticalAlignment, horizontalAlignment) {
    LiteRowMeasurePolicy(spaceBetweenItem, verticalAlignment, horizontalAlignment)
}

@Immutable
private class LiteRowMeasurePolicy(
    private val spaceBetweenItem: Dp,
    private val verticalAlignmentParent: VerticalAlignment,
    private val horizontalAlignmentParent: HorizontalAlignment
) : MeasurePolicy {

    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        val spaceAsPx = spaceBetweenItem.toPx().toInt()
        val alignments = arrayOfNulls<LiteRowChildDataNode>(measurables.size)

        val availableWidth = constraints.maxWidth
        var usedWidth = 0
        val minSizeComponent = 0
        val placeables = Array(measurables.size) { index ->
            alignments[index] = measurables[index].liteRowChildDataNode
            val remainingWidth =
                availableWidth - usedWidth - ((measurables.size - 1 - index) * spaceAsPx)

            val adjustedConstraints = constraints.copy(
                minWidth = minSizeComponent,
                maxWidth = remainingWidth.coerceIn(minSizeComponent, constraints.maxWidth)
            )
            val placeable = measurables[index].measure(adjustedConstraints)
            usedWidth += placeable.width + spaceAsPx
            placeable
        }

        val totalWidth = usedWidth - spaceAsPx.coerceAtLeast(0)
        val totalHeight = placeables.maxOfOrNull { it.height } ?: 0

        var xPosition = 0

        return layout(
            totalWidth.coerceIn(constraints.minWidth, constraints.maxWidth),
            totalHeight
        ) {
            for (index in placeables.indices) {
                val alignment = alignments.getOrNull(index)
                val placeable = placeables.getOrNull(index) ?: continue
                val targetVerticalAlignment =
                    alignment?.verticalAlignment ?: verticalAlignmentParent

                val yPosition = when (targetVerticalAlignment) {
                    VerticalAlignment.Top -> 0
                    VerticalAlignment.Center -> (totalHeight - placeable.height) / 2
                    VerticalAlignment.Bottom -> totalHeight - placeable.height
                }
                val targetHorizontalAlignment =
                    alignment?.horizontalAlignment ?: horizontalAlignmentParent

                val xPositionAdjusted = when (targetHorizontalAlignment) {
                    HorizontalAlignment.Start -> xPosition
                    HorizontalAlignment.Center -> (constraints.maxWidth - totalWidth) / 2 + xPosition
                    HorizontalAlignment.End -> constraints.maxWidth - totalWidth + xPosition
                }
                placeable.placeRelative(x = xPositionAdjusted, y = yPosition)
                xPosition += placeable.width + spaceAsPx
            }
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int
    ): Int {
        return measurables.fastMaxOfOrNull { maxIntrinsicHeightLambda(it, width) } ?: 0
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int
    ): Int {
        return measurables.fastMaxOfOrNull { minIntrinsicHeightLambda(it, width) } ?: 0
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        val childWidths = measurables.fastMap { maxIntrinsicWidthLambda(it, height) }

        val totalGapWidth = (childWidths.size - 1).coerceAtLeast(0) * spaceBetweenItem.roundToPx()

        return childWidths.sum() + totalGapWidth
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        val childWidths = measurables.fastMap { minIntrinsicWidthLambda(it, height) }

        val totalGapWidth = (childWidths.size - 1).coerceAtLeast(0) * spaceBetweenItem.roundToPx()

        return childWidths.sum() + totalGapWidth
    }
}

private val maxIntrinsicWidthLambda: IntrinsicMeasurable.(Int) -> Int = { maxIntrinsicWidth(it) }
private val minIntrinsicWidthLambda: IntrinsicMeasurable.(Int) -> Int = { minIntrinsicWidth(it) }
private val maxIntrinsicHeightLambda: IntrinsicMeasurable.(Int) -> Int = { maxIntrinsicHeight(it) }
private val minIntrinsicHeightLambda: IntrinsicMeasurable.(Int) -> Int = { minIntrinsicHeight(it) }
