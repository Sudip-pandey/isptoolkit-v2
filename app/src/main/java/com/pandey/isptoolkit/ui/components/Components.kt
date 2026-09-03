package com.pandey.isptoolkit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pandey.isptoolkit.ui.theme.*

enum class StatusBadgeType {
    PASS,
    WARNING,
    FAIL,
    UNAVAILABLE,
    INFO
}

@Composable
fun StatusChip(
    text: String,
    type: StatusBadgeType,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (type) {
        StatusBadgeType.PASS -> Pair(StatusPassGreen.copy(alpha = 0.15f), StatusPassGreen)
        StatusBadgeType.WARNING -> Pair(StatusWarningOrange.copy(alpha = 0.15f), StatusWarningOrange)
        StatusBadgeType.FAIL -> Pair(StatusFailRed.copy(alpha = 0.15f), StatusFailRed)
        StatusBadgeType.UNAVAILABLE -> Pair(StatusUnavailableGray.copy(alpha = 0.15f), StatusUnavailableGray)
        StatusBadgeType.INFO -> Pair(PrimaryBlue.copy(alpha = 0.15f), PrimaryBlueVariant)
    }

    Box(
        modifier = modifier
            .background(color = bgColor, shape = RoundedCornerShape(6.dp))
            .border(width = 1.dp, color = textColor.copy(alpha = 0.3f), shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text.uppercase(),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    unit: String = "",
    subtitle: String? = null,
    statusType: StatusBadgeType = StatusBadgeType.INFO,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = DarkSurface,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                StatusChip(text = statusType.name, type = statusType)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
            if (!subtitle.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
            }
        }
    }
}

@Composable
fun MetricRowItem(
    label: String,
    value: String?,
    fallbackText: String = "Unavailable"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Monospace
            )
        } else {
            Text(
                text = fallbackText,
                style = MaterialTheme.typography.bodyMedium,
                color = StatusUnavailableGray,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}
