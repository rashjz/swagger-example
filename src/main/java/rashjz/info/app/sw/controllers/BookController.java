package rashjz.info.app.sw.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rashjz.info.app.sw.annotations.DataAccessEvent;
import rashjz.info.app.sw.domain.Book;
import rashjz.info.app.sw.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;


    @GetMapping(value = "/listBooks")
    @DataAccessEvent( paramName = "page", eventKey = "PDF_Downloaded", eventDescription = "I9 PDF accessed")
    public String listBooks(Model model, @RequestParam(value = "page" ,required = false)  Integer page,
            @RequestParam(value = "size",required = false) Integer size) {

        log.info("listBooks request received with page {} size {}", page, size);

        int currentPage = Optional.ofNullable(page).orElse(1);
        int pageSize = Optional.ofNullable(size).orElse(5);

        Page<Book> bookPage = bookService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("bookPage", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "listBooks";
    }
}