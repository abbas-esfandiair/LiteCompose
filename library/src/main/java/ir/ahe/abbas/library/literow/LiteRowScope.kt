package ir.ahe.abbas.library.literow

import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.ParentDataModifierNode
import androidx.compose.ui.unit.Density

@Immutable
enum class VerticalAlignment {
    Top, Center, Bottom;
}

@Immutable
enum class HorizontalAlignment {
    Start, Center, End;
}

@LayoutScopeMarker
@Immutable
interface LiteRowScope {
    @Stable
    fun Modifier.align(
        verticalAlignment: VerticalAlignment = VerticalAlignment.Top,
        horizontalAlignment: HorizontalAlignment = HorizontalAlignment.Start
    ): Modifier
}

object LiteRowScopeInstance : LiteRowScope {
    @Stable
    override fun Modifier.align(
        verticalAlignment: VerticalAlignment,
        horizontalAlignment: HorizontalAlignment
    ): Modifier =
        this.then(LiteRowChildDataElement(verticalAlignment, horizontalAlignment))

}

@Immutable
private class LiteRowChildDataElement(
    val verticalAlignment: VerticalAlignment,
    val horizontalAlignment: HorizontalAlignment
) : ModifierNodeElement<LiteRowChildDataNode>() {

    override fun create(): LiteRowChildDataNode {
        return LiteRowChildDataNode(verticalAlignment, horizontalAlignment)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherModifier = other as? LiteRowChildDataElement ?: return false
        return verticalAlignment == otherModifier.verticalAlignment && horizontalAlignment == otherModifier.horizontalAlignment
    }

    override fun hashCode(): Int {
        return (verticalAlignment.hashCode() + horizontalAlignment.hashCode() * 31)
    }

    override fun update(node: LiteRowChildDataNode) {
        node.verticalAlignment = verticalAlignment
        node.horizontalAlignment = horizontalAlignment
    }
}

@Stable
val Measurable.liteRowChildDataNode: LiteRowChildDataNode? get() = parentData as? LiteRowChildDataNode

@Immutable
class LiteRowChildDataNode(
    var verticalAlignment: VerticalAlignment,
    var horizontalAlignment: HorizontalAlignment
) : ParentDataModifierNode, Modifier.Node() {
    override fun Density.modifyParentData(parentData: Any?) = this@LiteRowChildDataNode
}