package com.nima.openbooksdownloader.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.nima.openbooksdownloader.database.Book
import com.nima.openbooksdownloader.utils.Constants

@Composable
fun BookmarkItem(
    book: Book,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Card(
            shape = RoundedCornerShape(5.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            modifier = Modifier.padding(3.dp)
                .size(90.dp, 110.dp)
        ) {
            SubcomposeAsyncImage(model = Constants.imageBaseUrl+"${book.id}.jpg",
                contentDescription = null,
                loading = {
                    Surface(
                        modifier = Modifier.size(110.dp, 170.dp),
                        color = Color.Gray
                    ){

                    }
                },
                contentScale = ContentScale.FillBounds,
                error = {
                    Surface(
                        modifier = Modifier.size(110.dp, 170.dp),
                        color = Color.Gray
                    ){

                    }
                },
                success = {
                    SubcomposeAsyncImageContent()
                }
            )
        }
        ElevatedCard(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp, top = 3.dp, bottom = 3.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.elevatedCardElevation(10.dp)
        ) {
            Text(text = book.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            )
            Button(onClick = {
                onClick()
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp, start = 10.dp, end = 10.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(text = "See Details",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}