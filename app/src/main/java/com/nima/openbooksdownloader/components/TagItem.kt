package com.nima.openbooksdownloader.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nima.openbooksdownloader.database.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagItem (
    tag: Tag,
    onClick: () -> Unit,
    ){

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        onClick = {
            onClick()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = tag.name)
        }
    }
}