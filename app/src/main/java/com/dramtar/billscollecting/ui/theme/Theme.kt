import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define your custom orange accent color
val OrangeAccent = Color(0xFFFFA500) // Example: A bright orange

// Neutral color palette for light mode
val LightNeutralColorScheme = lightColorScheme(
    primary = Color(0xFF424242), // Dark gray for primary elements
    onPrimary = Color.White, // White text on primary elements
    primaryContainer = Color(0xFFE0E0E0), // Light gray for containers
    onPrimaryContainer = Color(0xFF212121), // Dark gray text on containers
    secondary = OrangeAccent, // Orange for secondary elements
    onSecondary = Color.White, // White text on secondary elements
    secondaryContainer = Color(0xFFFFE0B2), // Light orange for secondary containers
    onSecondaryContainer = Color(0xFF614000), // Dark orange text on secondary containers
    tertiary = Color(0xFF757575), // Medium gray for tertiary elements
    onTertiary = Color.White, // White text on tertiary elements
    tertiaryContainer = Color(0xFFF5F5F5), // Very light gray for tertiary containers
    onTertiaryContainer = Color(0xFF424242), // Dark gray text on tertiary containers
    background = Color(0xFFFAFAFA), // Very light gray for background
    onBackground = Color(0xFF212121), // Dark gray text on background
    surface = Color(0xFFFFFFFF), // White for surfaces
    onSurface = Color(0xFF212121), // Dark gray text on surfaces
    surfaceVariant = Color(0xFFEEEEEE), // Very light gray for surface variants
    onSurfaceVariant = Color(0xFF424242), // Dark gray text on surface variants
    outline = Color(0xFF9E9E9E), // Medium gray for outlines
    inverseSurface = Color(0xFF212121), // Dark gray for inverse surfaces
    inverseOnSurface = Color(0xFFFAFAFA), // Very light gray text on inverse surfaces
    inversePrimary = Color(0xFFE0E0E0), // Light gray for inverse primary elements
    surfaceTint = OrangeAccent, // Orange tint for surfaces
    outlineVariant = Color(0xFFBDBDBD), // Light gray for outline variants
    scrim = Color(0x99000000), // Semi-transparent black for scrims
)

// Neutral color palette for dark mode
val DarkNeutralColorScheme = darkColorScheme(
    primary = Color(0xFFE0E0E0), // Light gray for primary elements
    onPrimary = Color(0xFF212121), // Dark gray text on primary elements
    primaryContainer = Color(0xFF424242), // Dark gray for containers
    onPrimaryContainer = Color(0xFFE0E0E0), // Light gray text on containers
    secondary = OrangeAccent, // Orange for secondary elements
    onSecondary = Color(0xFF212121), // Dark gray text on secondary elements
    secondaryContainer = Color(0xFF614000), // Dark orange for secondary containers
    onSecondaryContainer = Color(0xFFFFE0B2), // Light orange text on secondary containers
    tertiary = Color(0xFFBDBDBD), // Light gray for tertiary elements
    onTertiary = Color(0xFF212121), // Dark gray text on tertiary elements
    tertiaryContainer = Color(0xFF757575), // Medium gray for tertiary containers
    onTertiaryContainer = Color(0xFFE0E0E0), // Light gray text on tertiary containers
    background = Color(0xFF121212), // Very dark gray for background
    onBackground = Color(0xFFFAFAFA), // Very light gray text on background
    surface = Color(0xFF212121), // Dark gray for surfaces
    onSurface = Color(0xFFFAFAFA), // Very light gray text on surfaces
    surfaceVariant = Color(0xFF424242), // Dark gray for surface variants
    onSurfaceVariant = Color(0xFFEEEEEE), // Very light gray text on surface variants
    outline = Color(0xFF757575), // Medium gray for outlines
    inverseSurface = Color(0xFFFAFAFA), // Very light gray for inverse surfaces
    inverseOnSurface = Color(0xFF212121), // Dark gray text on inverse surfaces
    inversePrimary = Color(0xFF424242), // Dark gray for inverse primary elements
    surfaceTint = OrangeAccent, // Orange tint for surfaces
    outlineVariant = Color(0xFF616161), // Dark gray for outline variants
    scrim = Color(0x99000000), // Semi-transparent black for scrims
)

@Composable
fun BillsCollectingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkNeutralColorScheme
        else -> LightNeutralColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}