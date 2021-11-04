package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Comment;
import com.example.demo.model.Product;
import com.example.demo.model.Transaction;
import com.example.demo.repasitory.ProductRepository;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CommentService;
import com.example.demo.service.ProductService;
import com.example.demo.service.TransactionService;
import com.example.demo.service.impl.ProductServiceImpl;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products")
@SessionAttributes("cart")
public class ProductController {

    private final ProductService productService;
    private final ProductServiceImpl productServiceimpl;
    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final TransactionService transactionService;
    private final CommentService commentService;

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    @GetMapping
    private String showProducts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "9") int size,
            @RequestParam(value = "orderBy", defaultValue = "title") String orderBy,
            @RequestParam(value = "order", defaultValue = "ASC") String order,
            ModelMap modelMap, Product product) {

        //todo sig nayel karoxa petq e hanel
        Sort sort = order.equals("ASC") ? Sort.by(Sort.Order.asc(orderBy)) :
                Sort.by(Sort.Order.desc(orderBy));

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Product> products = productServiceimpl.productpages(pageRequest);

        int totalPages = products.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());

            modelMap.addAttribute("pageNumbers", pageNumbers);
        }

        modelMap.addAttribute("products", products);
        log.info("User with {} username opened product page, product.size = {}",
                product.getId(),
                products.getSize());
        return "products";
    }

    @GetMapping("/{page}/{productsCount}")
    private String showProductsByPage(@PathVariable int page,
                                      @PathVariable int productsCount, Model model) {

        Pageable pageable = PageRequest.of(page, productsCount);
        List<Product> products = productService.getProductsByPage(pageable);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/{productId}")
    private String getProductById(@PathVariable Long productId, ModelMap modelMap) {
        Product product = productService.getProduct(productId);
        List<Comment> comments = commentService.getAllCommentsByPostId(productId);
        modelMap.addAttribute("comments", comments);
        modelMap.addAttribute("product", product);
        modelMap.addAttribute("amount", product.getPrice());
        modelMap.addAttribute("stripePublicKey", stripePublicKey);
        return "product";
    }

    @GetMapping("/add")
    private String createProduct(Model model) {

        List<Category> categories = categoryService.getCategories();
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categories);
        return "create-product";
    }

    @PostMapping("/{productId}/remove")
    public String blogPostDelet(@PathVariable Long productId, Model model) {
        Product product = productRepository.findById(productId).orElseThrow();
        productRepository.delete(product);
        return "redirect:/products";
    }

    @PostMapping("/add")
    private String addProduct(@Valid Product product, BindingResult bindingResult,
                              Model model, @AuthenticationPrincipal CurrentUser currentUser) {

        List<Category> categories = categoryService.getCategories();
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            return "create-product";
        }

        Long categoryId = product.getCategory().getId();
        Optional<Category> selectedCategory = categories
                .stream()
                .filter(category -> category.getId().equals(categoryId))
                .findFirst();

        product.setCategory(selectedCategory.get());

        product.setUser(currentUser.getUser());

        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/{productId}/edit")
    private String editProduct(@PathVariable Long productId, Model model,
                               SecurityContextHolderAwareRequestWrapper requestWrapper) {

        Product product = productService.getProduct(productId);
        model.addAttribute("product", product);
        requestWrapper.isUserInRole("ADMIN");
        return "edit-product";
    }

    @PostMapping("/{productId}")
    private String updateProduct(@PathVariable Long productId,
                                 Product formProduct) {

        Product product = productService.getProduct(productId);
        product.setDescription(formProduct.getDescription());
        product.setUrl(formProduct.getUrl());
        product.setPrice(formProduct.getPrice());
        productService.saveProduct(product);
        return String.format("redirect:/products/%d", productId);
    }

    @PostMapping("{productId}/charge")
    private String charge(@PathVariable Long productId, Transaction transaction,
                          RedirectAttributes redirectAttributes) throws StripeException {
        transactionService.charge(transaction);
        redirectAttributes.addFlashAttribute("message",
                "Payment successful");
        return String.format("redirect:/products/%d", productId);
    }


    @RequestMapping("/search")
    public String searchProducts(Model model, @Param("keyword") String keyword) {
        if (productRepository.search(keyword).isEmpty()) {
            return "redirect:/products";
        }
        List<Product> products = productServiceimpl.listAll(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        return "products";
    }

}
