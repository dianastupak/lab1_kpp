import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MeetingScheduler {
    static class TimeSlot {
        DayOfWeek day;
        ZonedDateTime startTime;
        ZonedDateTime endTime;

        TimeSlot(DayOfWeek day, ZonedDateTime startTime, ZonedDateTime endTime) {
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    static class User {
        String name;
        ZoneId timeZone;
        List<TimeSlot> availability;

        User(String name, ZoneId timeZone) {
            this.name = name;
            this.timeZone = timeZone;
            this.availability = new ArrayList<>();
        }

        void addAvailability(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
            ZonedDateTime start = ZonedDateTime.of(LocalDate.now(), startTime, timeZone);
            ZonedDateTime end = ZonedDateTime.of(LocalDate.now(), endTime, timeZone);
            availability.add(new TimeSlot(day, start, end));
        }
    }

    static class MeetingTime {
        DayOfWeek day;
        LocalTime startTime;
        Duration duration;

        MeetingTime(DayOfWeek day, LocalTime startTime, Duration duration) {
            this.day = day;
            this.startTime = startTime;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return String.format("%s %s (duration: %d hours)",
                    formatUkrainianDay(day),
                    startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    duration.toHours());
        }

        private String formatUkrainianDay(DayOfWeek day) {
            switch (day) {
                case MONDAY: return "MONDAY";
                case TUESDAY: return "TUESDAY";
                case WEDNESDAY: return "WEDNESDAY";
                case THURSDAY: return "THURSDAY";
                case FRIDAY: return "FRIDAY";
                case SATURDAY: return "SATURDAY";
                case SUNDAY: return "SUNDAY";
                default: return day.toString();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<User> users = new ArrayList<>();

        // Збір інформації про користувачів
        while (true) {
            System.out.println("\nEnter the name or if you want to exit - 0");
            String name = scanner.nextLine().trim();

            if (name.equalsIgnoreCase("0")) {
                break;
            }

            System.out.println("Enter the time zone (e.g., Europe/Kiev, America/New_York, Asia/Tokyo):");
            ZoneId timeZone = ZoneId.of(scanner.nextLine().trim());

            User user = new User(name, timeZone);

            while (true) {
                System.out.println("\nEnter the day of week " + name + " (1-7, where 1 = Monday, 0 - exit):");
                int dayNum = Integer.parseInt(scanner.nextLine());

                if (dayNum == 0) {
                    break;
                }

                if (dayNum < 1 || dayNum > 7) {
                    System.out.println("Incorrect data!");
                    continue;
                }

                System.out.println("Start time (HH:mm):");
                LocalTime startTime = LocalTime.parse(scanner.nextLine());

                System.out.println("End time (HH:mm):");
                LocalTime endTime = LocalTime.parse(scanner.nextLine());

                user.addAvailability(DayOfWeek.of(dayNum), startTime, endTime);
            }

            users.add(user);
        }

        System.out.println("\nEnter the required duration of the meeting (hours):");
        int duration = Integer.parseInt(scanner.nextLine());

        // Пошук спільного часу
        List<MeetingTime> possibleMeetings = findPossibleMeetings(users, duration);
        MeetingTime maxInterval = findMaxInterval(users);

        if (possibleMeetings.isEmpty()) {
            System.out.println("\nUnfortunately, a joint time was not found for all participants.");
        } else {
            System.out.println("\nPossible meeting options:");
            for (int i = 0; i < possibleMeetings.size(); i++) {
                System.out.println((i + 1) + ". " + possibleMeetings.get(i));
            }
        }

        System.out.println("\nMaximum possible meeting interval:");
        System.out.println(maxInterval);
    }

    private static List<MeetingTime> findPossibleMeetings(List<User> users, int durationHours) {
        List<MeetingTime> possibleMeetings = new ArrayList<>();
        Duration duration = Duration.ofHours(durationHours);

        // Для кожного дня тижня
        for (DayOfWeek day : DayOfWeek.values()) {
            List<TimeSlot> daySlots = new ArrayList<>();
            for (User user : users) {
                for (TimeSlot slot : user.availability) {
                    if (slot.day == day) {
                        daySlots.add(slot);
                    }
                }
            }

            if (daySlots.size() < users.size()) {
                continue;
            }

            LocalTime startTime = LocalTime.of(0, 0);
            LocalTime endTime = LocalTime.of(23, 59);

            for (TimeSlot slot : daySlots) {
                ZonedDateTime utcStart = slot.startTime.withZoneSameInstant(ZoneOffset.UTC);
                ZonedDateTime utcEnd = slot.endTime.withZoneSameInstant(ZoneOffset.UTC);

                if (utcStart.toLocalTime().isAfter(startTime)) {
                    startTime = utcStart.toLocalTime();
                }
                if (utcEnd.toLocalTime().isBefore(endTime)) {
                    endTime = utcEnd.toLocalTime();
                }
            }

            if (Duration.between(startTime, endTime).compareTo(duration) >= 0) {
                possibleMeetings.add(new MeetingTime(day, startTime, duration));
            }
        }

        return possibleMeetings;
    }

    private static MeetingTime findMaxInterval(List<User> users) {
        for (DayOfWeek day : DayOfWeek.values()) {
            LocalTime startTime = LocalTime.of(0, 0);
            LocalTime endTime = LocalTime.of(23, 59);

            for (User user : users) {
                for (TimeSlot slot : user.availability) {
                    if (slot.day == day) {
                        ZonedDateTime utcStart = slot.startTime.withZoneSameInstant(ZoneOffset.UTC);
                        ZonedDateTime utcEnd = slot.endTime.withZoneSameInstant(ZoneOffset.UTC);

                        if (utcStart.toLocalTime().isAfter(startTime)) {
                            startTime = utcStart.toLocalTime();
                        }
                        if (utcEnd.toLocalTime().isBefore(endTime)) {
                            endTime = utcEnd.toLocalTime();
                        }
                    }
                }
            }

            if (startTime.isBefore(endTime)) {
                return new MeetingTime(day, startTime, Duration.between(startTime, endTime));
            }
        }
        return null;
    }
}
