package com.github.charlesvhe.core.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<D, F> {
    private Collection<D> data;
    private PageRequest<F> next;
}
