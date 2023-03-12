package ru.ilin;

import java.util.List;

public record SearchResponse(List<String> response, String searchEngineName) {
}
