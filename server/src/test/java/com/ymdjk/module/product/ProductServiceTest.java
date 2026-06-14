package com.ymdjk.module.product;

import com.ymdjk.module.product.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @Test
    void testDetailWithNullId() {
        ProductService service = new ProductService(productMapper);
        assertNull(service.detail(null));
    }
}
