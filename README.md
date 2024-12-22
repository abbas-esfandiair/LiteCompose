# LiteCompose Library

The `LiteColumn` and `LiteRow` library provides lightweight and highly efficient layout components for Jetpack Compose. These components are designed to deliver enhanced performance for layout operations, making them ideal for use in scenarios requiring optimized rendering and layout calculations.

## Features

- **Efficient Layout Calculation:**
  - Optimized measure and layout processes.
  - Minimizes unnecessary recompositions.
  - Reduces constraints propagation overhead.

- **Intrinsic Measurements Support:**
  - Handles intrinsic width and height calculations efficiently.

- **Custom Alignment Options:**
  - Support for horizontal and vertical alignments.
  - Configurable spacing between items.

- **Composable-Friendly API:**
  - Easy-to-use, Composable-based syntax.

## Components

### LiteColumn
`LiteColumn` arranges its children in a vertical sequence, with options for horizontal alignment and spacing between items.

#### Usage
```kotlin
@Composable
fun ExampleLiteColumn() {
    LiteColumn(
        modifier = Modifier.fillMaxSize(),
        spaceBetweenItem = 8.dp,
        horizontalAlignment = HorizontalAlignment.Center
    ) {
        Text("Item 1")
        Text("Item 2")
        Text("Item 3")
    }
}
```

#### Parameters
- `modifier`: Modifies the layout's appearance or behavior (default: `Modifier`).
- `spaceBetweenItem`: Spacing between items (default: `0.dp`).
- `horizontalAlignment`: Horizontal alignment of items (default: `HorizontalAlignment.Start`).
- `content`: A Composable lambda to define child components.

### LiteRow
`LiteRow` arranges its children in a horizontal sequence, with options for vertical and horizontal alignment and spacing between items.

#### Usage
```kotlin
@Composable
fun ExampleLiteRow() {
    LiteRow(
        modifier = Modifier.fillMaxWidth(),
        spaceBetweenItem = 8.dp,
        verticalAlignment = VerticalAlignment.Center
    ) {
        Text("Item A")
        Text("Item B")
        Text("Item C")
    }
}
```

#### Parameters
- `modifier`: Modifies the layout's appearance or behavior (default: `Modifier`).
- `spaceBetweenItem`: Spacing between items (default: `0.dp`).
- `verticalAlignment`: Vertical alignment of items (default: `VerticalAlignment.Top`).
- `horizontalAlignment`: Horizontal alignment of items (default: `HorizontalAlignment.Start`).
- `content`: A Composable lambda to define child components.

## Alignment Options
Both `LiteColumn` and `LiteRow` support the following alignment options:

### HorizontalAlignment
- `Start`: Align items to the start.
- `Center`: Center-align items.
- `End`: Align items to the end.

### VerticalAlignment (LiteRow only)
- `Top`: Align items to the top.
- `Center`: Center-align items.
- `Bottom`: Align items to the bottom.

## Performance Considerations
The library optimizes rendering and layout processes by:
- Using tailored `MeasurePolicy` implementations.
- Avoiding unnecessary recompositions.
- Efficiently handling intrinsic measurements for child components.

## Installation
Add the library dependency to your project:

```gradle
dependencies {
    implementation "com.github.abbas7777:LiteCompose:1.0.0"
}
```

## License
This library is distributed under the MIT License. See the LICENSE file for more information.

---


