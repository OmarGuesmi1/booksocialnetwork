package org.example.booksocialnetwork.book;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.booksocialnetwork.common.PageResponse;
import org.example.booksocialnetwork.exception.OperationNotpermittedException;
import org.example.booksocialnetwork.history.BookTransactionHistory;
import org.example.booksocialnetwork.history.BookTransactionHistoryRepository;
import org.example.booksocialnetwork.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final BookMapper bookMapper;

    public Integer save(BookRequest request, Authentication connectedUser){
        User user = (User) connectedUser.getPrincipal();
        Book book = bookMapper.toBook(request);
        book.setOwner(user);
        return bookRepository.save(book).getId();
    }


    public BookResponse findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .map(bookMapper::toBookResponse)
                .orElseThrow(()->new EntityNotFoundException("No book found with the ID:: " + bookId));
    }

    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books= bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponse =books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()

        );
    }


    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<Book> books= bookRepository.findAll(BookSpecification.withOwnerId(user.getId()),pageable);

        List<BookResponse> bookResponse =books.stream()
                .map(bookMapper::toBookResponse)
                .toList();

        return new PageResponse<>(
                bookResponse,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()

        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> alloBorrowedBooks =bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponse = alloBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                alloBorrowedBooks.getNumber(),
                alloBorrowedBooks.getSize(),
                alloBorrowedBooks.getTotalElements(),
                alloBorrowedBooks.getTotalPages(),
                alloBorrowedBooks.isFirst(),
                alloBorrowedBooks.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> alloBorrowedBooks =bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
        List<BorrowedBookResponse> bookResponse = alloBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponse,
                alloBorrowedBooks.getNumber(),
                alloBorrowedBooks.getSize(),
                alloBorrowedBooks.getTotalElements(),
                alloBorrowedBooks.getTotalPages(),
                alloBorrowedBooks.isFirst(),
                alloBorrowedBooks.isLast()
        );
    }

    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getBooks(), user.getId())){
            throw new OperationNotpermittedException("You cannot update others books shareable status");

        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(book.getOwner().getId(), user.getId())){ // if the user is not the owner he cannot update a book
            throw new OperationNotpermittedException("You cannot update others books archived status");

        }
        book.setArchived(!book.isShareable());
        bookRepository.save(book);
        return bookId;
    }

    public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("No book found with ID:: " + bookId)); // that means in the data base u cannot found it

        if (book.isArchived() || !book.isShareable()){
            throw new OperationNotpermittedException("The requested book cannot be borrowed since it is archived or not shareable");
        }
        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(book.getOwner().getId(), user.getId())){ // if the user is the owner at the same time he cannot borrow a book
            throw new OperationNotpermittedException("You cannot borrow your own book");
        }
        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowedByUser(bookId,user.getId());
        if (isAlreadyBorrowed){
            throw new OperationNotpermittedException("The requested book us already borrowed");  // it means that u r not allowed to do it
        }
        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();

        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId();
    }
}
