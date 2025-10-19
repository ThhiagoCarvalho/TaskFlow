package com.example.myapplication233

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Modelo de tarefa
data class Task(
    val title: String,
    val priority: String,
    var completed: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Ativa tema Material3
            MaterialTheme {
                TaskFlowApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFlowApp() {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var title by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Média") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("TaskFlow – Gerenciador de Tarefas") })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Campo de texto
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título da tarefa") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                // Seleção de prioridade
                Row {
                    listOf("Alta", "Média", "Baixa").forEach { level ->
                        FilterChip(
                            selected = priority == level,
                            onClick = { priority = level },
                            label = { Text(level) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Botão adicionar
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            tasks = tasks + Task(title, priority)
                            title = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Adicionar Tarefa")
                }

                Spacer(Modifier.height(16.dp))

                // Lista de tarefas
                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(task) {
                            tasks = tasks.map {
                                if (it == task) it.copy(completed = !it.completed) else it
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun TaskItem(task: Task, onToggleComplete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.completed) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("${task.title} (${task.priority})")
            Checkbox(checked = task.completed, onCheckedChange = { onToggleComplete() })
        }
    }
}
