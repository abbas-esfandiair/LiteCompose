package ir.ahe.abbas.library.litecolumn

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ParentDataModifierNode
import androidx.compose.ui.unit.Density

@Immutable
enum class HorizontalAlignment {
    Start, Center, End
}

@LayoutScopeMarker
@Immutable
interface LiteColumnScope {
    @Stable
    fun Modifier.align(alignment: HorizontalAlignment): Modifier
}

internal object LiteColumnScopeInstance : LiteColumnScope {

    @Stable
    override fun Modifier.align(alignment: HorizontalAlignment): Modifier =
        this.then(LiteColumnChildDataElement(alignment))

}

@SuppressLint("ModifierNodeInspectableProperties")
@Immutable
private class LiteColumnChildDataElement(
    val alignment: HorizontalAlignment
) : ModifierNodeElement<LiteColumnChildDataNode>() {

    override fun create(): LiteColumnChildDataNode {
        return LiteColumnChildDataNode(alignment)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherModifier = other as? LiteColumnChildDataElement ?: return false
        return alignment == otherModifier.alignment
    }

    override fun hashCode(): Int {
        return alignment.hashCode()
    }

    override fun update(node: LiteColumnChildDataNode) {
        node.alignment = alignment
    }
}

@Stable
val Measurable.liteColumnChildDataNode: LiteColumnChildDataNode? get() = parentData as? LiteColumnChildDataNode

@Immutable
class LiteColumnChildDataNode(
    var alignment: HorizontalAlignment
) : ParentDataModifierNode, Modifier.Node() {
    override fun Density.modifyParentData(parentData: Any?) = this@LiteColumnChildDataNode
}