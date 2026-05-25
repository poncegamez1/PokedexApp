package com.example.pokedexapp.ui.screens.passwordgeneratorscreen

import android.content.ClipData
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokedexapp.R
import com.example.pokedexapp.ui.theme.MediumColor
import com.example.pokedexapp.ui.theme.StrongColor
import com.example.pokedexapp.ui.theme.WeakColor
import kotlinx.coroutines.launch

/*
I chose SecureRandom because it is the industry standard for security.
While other methods use basic math calculations that a clever computer program can eventually predict,
SecureRandom taps into unpredictable device background noise to guarantee that every password
generated is truly unique, unguessable, and safe for production use.
 */

@Composable
fun PasswordGeneratorScreen(
    length: Int,
    useUppercase: Boolean,
    useNumbers: Boolean,
    useSymbols: Boolean,
    password: String,
    copied: Boolean,
    strength: PasswordStrength,
    onLengthChange: (Int) -> Unit,
    onUppercaseToggle: (Boolean) -> Unit,
    onNumbersToggle: (Boolean) -> Unit,
    onSymbolsToggle: (Boolean) -> Unit,
    onGeneratePassword: () -> Unit,
    onTriggerCopyFeedback: () -> Unit
) {
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.pg_title),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            LengthSlider(
                length = length,
                onLengthChange = onLengthChange
            )

            CharacterClassToggles(
                useUppercase = useUppercase,
                onUppercaseChange = onUppercaseToggle,
                useNumbers = useNumbers,
                onNumbersChange = onNumbersToggle,
                useSymbols = useSymbols,
                onSymbolsChange = onSymbolsToggle
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            PasswordActions(
                copied = copied,
                isPasswordGenerated = password.isNotEmpty(),
                onRegenerate = onGeneratePassword,
                onCopy = {
                    if (password.isNotEmpty()) {
                        val clipData = ClipData.newPlainText("password", password)
                        scope.launch {
                            clipboardManager.setClipEntry(clipData.toClipEntry())
                            onTriggerCopyFeedback()
                        }
                    }
                }
            )

            PasswordDisplay(password = password)

            PasswordStrengthIndicator(strength = strength)
        }
    }
}

@Composable
private fun PasswordDisplay(password: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Text(
            text = password.ifEmpty { stringResource(R.string.pg_password_empty) },
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.06.sp,
            lineHeight = 28.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun PasswordStrengthIndicator(strength: PasswordStrength, modifier: Modifier = Modifier) {
    val strengthColor by animateColorAsState(
        targetValue = when (strength) {
            PasswordStrength.WEAK -> WeakColor
            PasswordStrength.MEDIUM -> MediumColor
            PasswordStrength.STRONG -> StrongColor
        },
        animationSpec = tween(400), label = "strengthColor"
    )
    val strengthFraction by animateFloatAsState(
        targetValue = when (strength) {
            PasswordStrength.WEAK -> 0.33f
            PasswordStrength.MEDIUM -> 0.66f
            PasswordStrength.STRONG -> 1.00f
        },
        animationSpec = tween(400), label = "strengthFraction"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.outlineVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(strengthFraction)
                    .clip(RoundedCornerShape(2.dp))
                    .background(strengthColor)
            )
        }
        Text(
            text = strength.name.lowercase().replaceFirstChar { it.uppercase() },
            color = strengthColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(min = 54.dp),
        )
    }
}

@Composable
private fun PasswordActions(
    copied: Boolean,
    isPasswordGenerated: Boolean,
    onRegenerate: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onRegenerate,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                stringResource(
                    if (isPasswordGenerated) R.string.pg_action_regenerate else R.string.pg_action_generate
                )
            )
        }
        OutlinedButton(
            onClick = onCopy,
            enabled = isPasswordGenerated,
            modifier = Modifier.weight(1f),
            colors = if (copied) ButtonDefaults.outlinedButtonColors(
                containerColor = StrongColor.copy(alpha = 0.08f)
            ) else ButtonDefaults.outlinedButtonColors()
        ) {
            Icon(
                Icons.Outlined.ContentCopy,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (copied) StrongColor
                else LocalContentColor.current.copy(alpha = if (isPasswordGenerated) 1f else 0.38f)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(if (copied) R.string.pg_action_copied else R.string.pg_action_copy),
                color = if (copied) StrongColor else LocalContentColor.current
            )
        }
    }
}

@Composable
private fun LengthSlider(
    length: Int,
    onLengthChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.pg_length_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                length.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        Slider(
            value = length.toFloat(),
            onValueChange = { onLengthChange(it.toInt()) },
            valueRange = 4f..64f,
            steps = 59,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(R.string.pg_slider_min),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                stringResource(R.string.pg_slider_max),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CharacterClassToggles(
    useUppercase: Boolean, onUppercaseChange: (Boolean) -> Unit,
    useNumbers: Boolean, onNumbersChange: (Boolean) -> Unit,
    useSymbols: Boolean, onSymbolsChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            stringResource(R.string.pg_character_classes_label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OptionToggle(
            label = stringResource(R.string.pg_toggle_uppercase),
            subLabel = stringResource(R.string.pg_toggle_uppercase_sub),
            checked = useUppercase,
            onChange = onUppercaseChange,
        )
        OptionToggle(
            label = stringResource(R.string.pg_toggle_numbers),
            subLabel = stringResource(R.string.pg_toggle_numbers_sub),
            checked = useNumbers,
            onChange = onNumbersChange,
        )
        OptionToggle(
            label = stringResource(R.string.pg_toggle_symbols),
            subLabel = stringResource(R.string.pg_toggle_symbols_sub),
            checked = useSymbols,
            onChange = onSymbolsChange,
        )
    }
}

@Composable
private fun OptionToggle(
    label: String,
    subLabel: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                label, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                subLabel, style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = checked, onCheckedChange = onChange)
    }
}

enum class PasswordStrength {
    WEAK,
    MEDIUM,
    STRONG
}