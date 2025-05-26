import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class App {
    private static final Map<String, List<String>> genreMovies = new HashMap<>();
    private final Set<Integer> bookedSeats = new HashSet<>();

    static {
        genreMovies.put("Action", Arrays.asList("Avengers", "The Dark Knight", "Iron Man", "Thor", "Captain America", "Guardians of the Galaxy", "Deadpool", "Logan", "Wonder Woman", "Aquaman", "Justice League", "Batman Begins", "Mission Impossible", "Fast & Furious", "John Wick", "Mad Max", "King Kong"));
        genreMovies.put("Sci-Fi", Arrays.asList("Inception", "Interstellar", "Avatar", "The Matrix", "Jurassic Park", "Star Wars", "Tenet"));
        genreMovies.put("Drama", Arrays.asList("Titanic", "Forrest Gump", "The Godfather", "The Prestige", "Memento", "The Revenant", "La La Land"));
        genreMovies.put("Animation", Arrays.asList("Frozen", "Toy Story", "Finding Nemo", "The Lion King", "Up", "Coco", "Inside Out", "Soul", "Moana", "Zootopia"));
        genreMovies.put("Fantasy", Arrays.asList("Harry Potter", "Aladdin", "Beauty and the Beast", "Cinderella", "Maleficent", "Pirates of the Caribbean"));
        genreMovies.put("Comedy", Arrays.asList("Shrek", "Pulp Fiction"));

        Set<String> allMovies = new LinkedHashSet<>();
        for (List<String> list : genreMovies.values()) {
            allMovies.addAll(list);
        }
        genreMovies.put("All", new ArrayList<>(allMovies));
    }

    public static void main(String[] args) {
        new App().createUI();
    }

    void createUI() {
        JFrame frame = new JFrame("Movie Ticket Booking");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 480);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel genreLabel = new JLabel("Select Genre:");
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<String> genreBox = new JComboBox<>(genreMovies.keySet().toArray(new String[0]));
        genreBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        genreBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel movieLabel = new JLabel("Select Movie:");
        movieLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        List<String> allMovies = genreMovies.get("All");
        JComboBox<String> movieBox = new JComboBox<>(allMovies.toArray(new String[0]));
        movieBox.setEditable(true);
        movieBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        movieBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        List<String> currentMovieList = new ArrayList<>(allMovies);

        genreBox.addActionListener(e -> {
            String selectedGenre = (String) genreBox.getSelectedItem();
            List<String> movies = genreMovies.get(selectedGenre);
            currentMovieList.clear();
            currentMovieList.addAll(movies);
            movieBox.removeAllItems();
            for (String m : movies) movieBox.addItem(m);
        });

        JTextField movieEditor = (JTextField) movieBox.getEditor().getEditorComponent();
        movieEditor.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String input = movieEditor.getText();
                movieBox.removeAllItems();
                for (String movie : currentMovieList) {
                    if (movie.toLowerCase().contains(input.toLowerCase())) {
                        movieBox.addItem(movie);
                    }
                }
                movieEditor.setText(input);
                movieBox.setPopupVisible(true);
            }
        });

        JLabel seatLabel = new JLabel("Select Seat Row:");
        seatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] seatRows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        JComboBox<String> seatBox = new JComboBox<>(seatRows);
        seatBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        seatBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel ticketLabel = new JLabel("Number of Tickets:");
        ticketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField ticketField = new JTextField();
        ticketField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        ticketField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton bookButton = new JButton("Book Tickets");
        bookButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel resultLabel = new JLabel(" ");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(new Color(0, 102, 0));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel.add(genreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(genreBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(movieLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(movieBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(seatLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(seatBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(ticketLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(ticketField);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(bookButton);
        panel.add(resultLabel);

        bookButton.addActionListener(e -> {
            String movie = (String) movieBox.getSelectedItem();
            String tickets = ticketField.getText().trim();
            String seatRow = (String) seatBox.getSelectedItem();

            int price;
            if (seatRow.equals("A") || seatRow.equals("B") || seatRow.equals("C")) {
                price = 500;
            } else if (seatRow.equals("D") || seatRow.equals("E") || seatRow.equals("F")) {
                price = 350;
            } else {
                price = 200;
            }

            if (tickets.matches("\\d+") && Integer.parseInt(tickets) > 0) {
                int numTickets = Integer.parseInt(tickets);

                int rowIndex = seatRow.charAt(0) - 'A';
                int startSeat = rowIndex * 10 + 1;
                int endSeat = startSeat + 9;

                List<Integer> availableSeats = new ArrayList<>();
                for (int i = startSeat; i <= endSeat; i++) {
                    if (!bookedSeats.contains(i)) {
                        availableSeats.add(i);
                    }
                }

                if (availableSeats.size() < numTickets) {
                    resultLabel.setText("Not enough seats in row " + seatRow + ". Only " + availableSeats.size() + " available.");
                    return;
                }

                Collections.shuffle(availableSeats);
                List<Integer> allocated = new ArrayList<>();
                for (int i = 0; i < numTickets; i++) {
                    int seat = availableSeats.get(i);
                    bookedSeats.add(seat);
                    allocated.add(seat);
                }

                Collections.sort(allocated);
                int total = numTickets * price;
                resultLabel.setText("<html>Booked " + numTickets + " tickets for " + movie +
                        " (Row " + seatRow + ")<br>Seats: " + allocated +
                        "<br>Total: Rs. " + total + "</html>");
            } else {
                resultLabel.setText("Please enter a valid number of tickets.");
            }
        });

        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
