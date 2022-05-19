package com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.components

import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mutualmobile.harvestKmp.android.R
import com.mutualmobile.harvestKmp.android.ui.theme.ReportCardTypography
import com.mutualmobile.harvestKmp.android.ui.theme.TertiaryColor
import com.mutualmobile.harvestKmp.android.ui.theme.TertiaryInverseColor
import com.mutualmobile.harvestKmp.android.ui.utils.toDecimalString

@Composable
fun BillableHoursCard(
    @IntRange(from = 0, to = 100)
    percentage: Int,
    billableHours: Float,
    nonBillableHours: Float
) {
    ReportScreenCard {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.reports_screen_billable_hours_title))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = percentage.div(100).toFloat(),
                        color = TertiaryColor,
                        modifier = Modifier.size(54.dp),
                        strokeWidth = 7.dp
                    )
                    Text(text = buildAnnotatedString {
                        append(percentage.toString())
                        withStyle(
                            SpanStyle(
                                fontSize = 10.sp,
                                baselineShift = BaselineShift.Superscript,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append("%")
                        }
                    }, style = ReportCardTypography.body2)
                }
                Column {
                    BillableTypeRow(
                        color = TertiaryColor,
                        title = stringResource(R.string.report_screen_billable_title),
                        value = billableHours.toDecimalString()
                    )
                    Spacer(modifier = Modifier.padding(vertical = 2.dp))
                    BillableTypeRow(
                        color = TertiaryInverseColor,
                        title = stringResource(R.string.report_screen_non_billable_title),
                        value = nonBillableHours.toDecimalString()
                    )
                }
            }
        }
    }
}