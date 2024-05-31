package com.example.teamcity.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VscRootEntries {
    private int count;
    private VscRootEntry[] vscRootEntry;
}
