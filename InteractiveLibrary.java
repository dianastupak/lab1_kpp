import java.util.ArrayList;  // Імпортуємо клас ArrayList для використання динамічних списків
import java.util.HashMap;    // Імпортуємо клас HashMap для створення хеш-мапи
import java.util.List;       // Імпортуємо інтерфейс List для використання списків
import java.util.Map;        // Імпортуємо інтерфейс Map для роботи з мапами
import java.util.Comparator; // Імпортуємо клас Comparator для сортування
import java.util.Set;        // Імпортуємо інтерфейс Set для роботи з множинами
import java.util.Optional;   // Імпортуємо клас Optional для роботи з необов'язковими значеннями
import java.util.stream.Collectors; // Імпортуємо клас Collectors для збирання даних у колекції
import java.util.Collections;
import java.util.HashSet;
import java.util.Set; 
import java.util.Scanner;

class Book { 
    private String title;           // назви книги
    private String author;          // автора книги
    private String genre;           // жанру книги
    private int publicationYear;    // Пол1е для зберігання року публікації
    private int pageCount;          // Поле для зберігання кількості сторінок
    private boolean isRare;         // Поле для визначення, чи є книга рідкісною

    public Book(String title, String author, String genre, int publicationYear, int pageCount, boolean isRare) {
        // Конструктор класу Book для ініціалізації полів об'єкта
        this.title = title;  // посилання на конкретний обєкт класу, значення параметра присвоюється полю title
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.pageCount = pageCount;
        this.isRare = isRare;
    }

    public String getGenre() {
        return genre;  // Метод повертає жанр книги
    }

    public String getAuthor() {
        return author;  // Метод повертає автора книги
    }

    public int getPublicationYear() {
        return publicationYear;  // Метод повертає рік публікації книги
    } 

    public int getPageCount() {
        return pageCount;  // Метод повертає кількість сторінок книги
    }

    public boolean isRare() {
        return isRare;  // Метод повертає true, якщо книга рідкісна, і false - якщо ні
    }

    public String getTitle() {
        return title;  // Метод повертає назву книги
    }

    @Override //перевантаження методу, для отримання текстового представлення рядка 
    public String toString() {
        // Метод для представлення книги у вигляді рядка
        return "Title: " + title + ", Author: " + author + ", Genre: " + genre +
                ", Year: " + publicationYear + ", Pages: " + pageCount + ", Rare: " + (isRare ? "Yes" : "No");
    }
}

class Library {  // Оголошення класу Library, що представляє бібліотеку
    private List<Book> books;  // Список для зберігання книг у бібліотеці

    public Library() {
        books = new ArrayList<>();  // Ініціалізація списку книг у конструкторі
    }

    public void addBook(Book book) {
        books.add(book);  // Метод для додавання книги до бібліотеки
    }

    public List<Book> getRareBooks() {
        // Метод для отримання списку рідкісних книг
        List<Book> rareBooks = new ArrayList<>();  // Створення списку для рідкісних книг
        for (Book book : books) {  // Перебір усіх книг у бібліотеці
            if (book.isRare()) {  // Якщо книга рідкісна, додаємо її до списку
                rareBooks.add(book);
            }
        }
        return rareBooks;  // Повертаємо список рідкісних книг
    }

    public List<Book> getRareBooksStream() {
        // Метод для отримання списку рідкісних книг з використанням Stream API
        return books.stream()  // Створюємо потік з колекції книг
                    .filter(Book::isRare)  // Фільтруємо тільки рідкісні книги, пояилання на метод 
                    .collect(Collectors.toList());  // Збираємо результати в список і повертаємо
    }

    
    public List<Book> getCommonBooks() {
        // Метод для отримання списку звичайних (нерідкісних) книг
        List<Book> commonBooks = new ArrayList<>();  // Створення списку для звичайних книг
        for (Book book : books) {  // Перебір усіх книг у бібліотеці
            if (!book.isRare()) {  // Якщо книга не є рідкісною, додаємо її до списку
                commonBooks.add(book);
            }
        }
        return commonBooks;  // Повертаємо список звичайних книг
    }

    public List<Book> getCommonBooksStream() {
        // Метод для отримання списку звичайних (нерідкісних) книг з використанням Stream API
        return books.stream()  // Створюємо потік з колекції книг
                    .filter(book -> !book.isRare())  // Фільтруємо ті книги, які не є рідкісними
                    .collect(Collectors.toList());  // Збираємо результати в список і повертаємо
    }
    

    public Map<String, List<Book>> getBooksGroupedByGenre() {
        // Метод для групування книг за жанрами
        Map<String, List<Book>> booksByGenre = new HashMap<>();  // Створюємо мапу для зберігання книг за жанрами
        for (Book book : books) {  // Перебір усіх книг у бібліотеці
            booksByGenre
                .computeIfAbsent(book.getGenre(), k -> new ArrayList<>())  // Якщо жанр ще не додано до мапи, додаємо його
                .add(book);  // Додаємо книгу до відповідного жанру
        }
        return booksByGenre;  // Повертаємо мапу, згруповану за жанрами
    }

    public Map<String, List<Book>> getBooksGroupedByGenreStream() {
        // Метод для групування книг за жанрами з використанням Stream API
        return books.stream()  // Створюємо потік з колекції книг
                    .collect(Collectors.groupingBy(Book::getGenre));  // Групуємо книги за жанрами та збираємо в мапу
    }

    public Map<String, Double> calculateAveragePagesPerGenre() {
        Map<String, Integer> pageSumByGenre = new HashMap<>();
        Map<String, Integer> bookCountByGenre = new HashMap<>();
        
        // Проходимо по списку книг
        for (Book book : books) {
            String genre = book.getGenre();
            int pageCount = book.getPageCount();
            
            // Оновлюємо суму сторінок для жанру
            pageSumByGenre.put(genre, pageSumByGenre.getOrDefault(genre, 0) + pageCount);
            
            // Оновлюємо кількість книг для жанру
            bookCountByGenre.put(genre, bookCountByGenre.getOrDefault(genre, 0) + 1);
        }
        
        // Створюємо карту для збереження середньої кількості сторінок для кожного жанру
        Map<String, Double> averagePagesByGenre = new HashMap<>();
        
        // Обчислюємо середнє значення для кожного жанру
        for (String genre : pageSumByGenre.keySet()) {
            double average = (double) pageSumByGenre.get(genre) / bookCountByGenre.get(genre);
            averagePagesByGenre.put(genre, average);
        }
        
        return averagePagesByGenre;
    }
    
    public Map<String, Double> calculateAveragePagesPerGenreStream() {
        // Метод для обчислення середньої кількості сторінок для кожного жанру
        return books.stream()  // Створюємо потік з колекції книг
                    .collect(Collectors.groupingBy(Book::getGenre,  // Групуємо книги за жанрами
                            Collectors.averagingInt(Book::getPageCount)));  // Обчислюємо середню кількість сторінок для кожного жанру
    }
    

    public List<Book> sortBooksByYearAndPages() {
        // Створюємо копію списку, щоб не змінювати оригінальний
        List<Book> sortedBooks = new ArrayList<>(books);
        
        // Використовуємо метод Collections.sort з компаратором для сортування
        Collections.sort(sortedBooks, new Comparator<Book>() {
            @Override
            public int compare(Book b1, Book b2) {
                int yearComparison = Integer.compare(b1.getPublicationYear(), b2.getPublicationYear());
                if (yearComparison != 0) {
                    return yearComparison;  // Якщо роки різні, сортуємо за роком
                }
                return Integer.compare(b1.getPageCount(), b2.getPageCount());  // Якщо роки однакові, сортуємо за кількістю сторінок
            }
        });
        
        return sortedBooks;  // Повертаємо відсортований список
    }
    

    public List<Book> sortBooksByYearAndPagesStream() {
        // Метод для сортування книг за роком видання та кількістю сторінок
        return books.stream()
                    .sorted(Comparator.comparingInt(Book::getPublicationYear)  // Сортуємо за роком видання
                            .thenComparingInt(Book::getPageCount))  // Додатково сортуємо за кількістю сторінок
                    .collect(Collectors.toList());  // Збираємо відсортований список
    }
    
    public Set<String> getUniqueAuthors() {// Метод для отримання списку унікальних авторів
        Set<String> uniqueAuthors = new HashSet<>();  // Створюємо множину для унікальних авторів
        
        // Проходимо по списку книг
        for (Book book : books) {
            uniqueAuthors.add(book.getAuthor());  // Додаємо автора до множини
        }
        
        return uniqueAuthors;  // Повертаємо множину унікальних авторів
    }
    

    public Set<String> getUniqueAuthorsStream() {
        // Метод для отримання списку унікальних авторів
        return books.stream()  // Створюємо потік з колекції книг
                    .map(Book::getAuthor)  // Отримуємо автора кожної книги
                    .collect(Collectors.toSet());  // Збираємо у множину (Set), що забезпечує унікальність
    }
    
    public Optional<Book> findBookWithMostPages() {
        if (books == null || books.isEmpty()) {
            return Optional.empty();  // Якщо список книг порожній, повертаємо пустий Optional
        }
    
        Book bookWithMostPages = null;
    
        // Проходимо по всіх книгах
        for (Book book : books) {
            if (bookWithMostPages == null || book.getPageCount() > bookWithMostPages.getPageCount()) {
                bookWithMostPages = book;  // Зберігаємо книгу з більшою кількістю сторінок
            }
        }
    
        return Optional.ofNullable(bookWithMostPages);  // Повертаємо результат, використовуючи Optional
    }

    public Optional<Book> findBookWithMostPagesSteram() {
        // Метод для знаходження книги з найбільшою кількістю сторінок
        return books.stream()  // Створюємо потік з колекції книг
                    .max(Comparator.comparingInt(Book::getPageCount));  // Знаходимо книгу з максимальною кількістю сторінок
    }
    
    public void printBooks(List<Book> books) {
        // Метод для виведення списку книг
        for (Book book : books) {  // Перебір усіх книг у списку
            System.out.println(book);  // Виведення інформації про книгу
        }
    }

    public void printBooksStream(List<Book> books) {
        // Метод для виведення списку книг з використанням Stream API
        books.forEach(System.out::println);  // Використовуємо метод forEach для виведення кожної книги
    }
    

    public void printBooksByGenre() {
        // Метод для виведення книг, згрупованих за жанрами
        Map<String, List<Book>> booksByGenre = getBooksGroupedByGenre();  // Отримуємо мапу, згруповану за жанрами
        for (Map.Entry<String, List<Book>> entry : booksByGenre.entrySet()) {  // Перебираємо кожен жанр у мапі
            System.out.println("\nGenre: " + entry.getKey());  // Виводимо назву жанру
            printBooks(entry.getValue());  // Виводимо список книг цього жанру
        }
    }
}

public class InteractiveLibrary{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        // Створення об'єктів книг
        Book book1 = new Book("Ulysses", "James Joyce", "Novel", 1922, 730, true);
        Book book2 = new Book("Moby Dick", "Herman Melville", "Adventure", 1851, 635, false);
        Book book3 = new Book("First Folio", "William Shakespeare", "Drama", 1623, 1000, true);
        Book book4 = new Book("Second Folio", "William Shakespeare", "Drama", 1523, 1999, true);
        Book book5 = new Book("The Great Gatsby", "F. Scott Fitzgerald", "Novel", 1925, 218, false);

        // Додавання книг до бібліотеки
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);

        boolean running = true;

        while (running) {
            // Запит на вибір операції
            System.out.println("Choose the number:");
            System.out.println("1. Print the rare books");
            System.out.println("2. Print the ordinary books");
            System.out.println("3. Print the books, grouped by genre");
            System.out.println("4. Calculate the average number of pages for each genre");
            System.out.println("5. Print sorted books by year and number of pages");
            System.out.println("6. Print a list of unique authors");
            System.out.println("7. Find the book with the most pages");
            System.out.println("8. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Очищення після введення числа

            if (choice >= 1 && choice <= 7) {
                // Запит на використання Stream API
                System.out.println("Used Stream API? (yes/no)");
                String useStream = scanner.nextLine().trim().toLowerCase();
                boolean isStream = useStream.equals("yes");

                switch (choice) {
                    case 1:
                        System.out.println("Rare books:");
                        if (isStream) {
                            library.printBooks(library.getRareBooksStream());
                        } else {
                            library.printBooks(library.getRareBooks());
                        }
                        break;

                    case 2:
                        System.out.println("Ordinary books:");
                        if (isStream) {
                            library.printBooks(library.getCommonBooksStream());
                        } else {
                            library.printBooks(library.getCommonBooks());
                        }
                        break;

                    case 3:
                        System.out.println("Books, grouped by genre:");
                        if (isStream) {
                            library.printBooksByGenre(); // Використання Stream API вже вбудовано в цей метод
                        } else {
                            library.printBooksByGenre(); // Використання звичайного способу
                        }
                        break;

                    case 4:
                        System.out.println("Average number of pages for each genre:");
                        Map<String, Double> avgPages;
                        if (isStream) {
                            avgPages = library.calculateAveragePagesPerGenreStream();
                        } else {
                            avgPages = library.calculateAveragePagesPerGenre();
                        }
                        for (Map.Entry<String, Double> entry : avgPages.entrySet()) {
                            System.out.printf("Genre: %s, Average number of pages: %.2f%n", entry.getKey(), entry.getValue());
                        }
                        break;

                    case 5:
                        System.out.println("Sorted books by year and number of pages:");
                        List<Book> sortedBooks;
                        if (isStream) {
                            sortedBooks = library.sortBooksByYearAndPagesStream();
                        } else {
                            sortedBooks = library.sortBooksByYearAndPages();
                        }
                        library.printBooks(sortedBooks);
                        break;

                    case 6:
                        System.out.println("Unique authors:");
                        Set<String> uniqueAuthors;
                        if (isStream) {
                            uniqueAuthors = library.getUniqueAuthorsStream();
                        } else {
                            uniqueAuthors = library.getUniqueAuthors();
                        }
                        uniqueAuthors.forEach(System.out::println);
                        break;

                    case 7:
                        System.out.println("The book with the most pages:");
                        Optional<Book> bookWithMostPages;
                        if (isStream) {
                            bookWithMostPages = library.findBookWithMostPagesSteram();
                        } else {
                            bookWithMostPages = library.findBookWithMostPages();
                        }
                        bookWithMostPages.ifPresentOrElse(
                                book -> System.out.println("Name: " + book.getTitle() + ", Pages: " + book.getPageCount()),
                                () -> System.out.println("There are no books.")
                        );
                        break;

                    default:
                        System.out.println("Incorrect operation selection.");
                }
            } else if (choice == 8) {
                System.out.println("The end");
                running = false; // Завершити цикл
            } else {
                System.out.println("Incorrect operation selection.");
            }
        }

        scanner.close(); // Закриваємо сканер після завершення програми
    }
}