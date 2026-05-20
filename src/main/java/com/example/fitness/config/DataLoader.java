package com.example.fitness.config;

import com.example.fitness.model.*;
import com.example.fitness.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;
    private final CourseRepository courseRepository;
    private final CourseRegistrationRepository registrationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded – skipping DataLoader.");
            return;
        }

        log.info("Seeding database...");

        User adminUser = userRepository.save(User.builder()
                .email("admin@fitnessstudio.de")
                .password(passwordEncoder.encode("admin123"))
                .role(UserRole.ADMIN)
                .build());

        User staffUser = userRepository.save(User.builder()
                .email("empfang@fitnessstudio.de")
                .password(passwordEncoder.encode("staff123"))
                .role(UserRole.STAFF)
                .build());

        User memberUser1 = userRepository.save(User.builder()
                .email("anna.mueller@email.de")
                .password(passwordEncoder.encode("member123"))
                .role(UserRole.MEMBER)
                .build());

        User memberUser2 = userRepository.save(User.builder()
                .email("tom.schneider@email.de")
                .password(passwordEncoder.encode("member123"))
                .role(UserRole.MEMBER)
                .build());

        User memberUser3 = userRepository.save(User.builder()
                .email("lena.braun@email.de")
                .password(passwordEncoder.encode("member123"))
                .role(UserRole.MEMBER)
                .build());

        Member anna = memberRepository.save(Member.builder()
                .memberNumber("M-2026-00001")
                .firstName("Anna")
                .lastName("Müller")
                .email("anna.mueller@email.de")
                .phone("+49 151 11223344")
                .birthDate(LocalDate.of(1992, 4, 15))
                .gender(Gender.FEMALE)
                .contractModel(ContractModel.PREMIUM)
                .contractStart(LocalDate.of(2026, 1, 1))
                .status(MemberStatus.ACTIVE)
                .user(memberUser1)
                .build());

        Member tom = memberRepository.save(Member.builder()
                .memberNumber("M-2026-00002")
                .firstName("Tom")
                .lastName("Schneider")
                .email("tom.schneider@email.de")
                .phone("+49 160 99887766")
                .birthDate(LocalDate.of(1988, 9, 3))
                .gender(Gender.MALE)
                .contractModel(ContractModel.BASIC)
                .contractStart(LocalDate.of(2026, 2, 1))
                .status(MemberStatus.ACTIVE)
                .user(memberUser2)
                .build());

        Member lena = memberRepository.save(Member.builder()
                .memberNumber("M-2026-00003")
                .firstName("Lena")
                .lastName("Braun")
                .email("lena.braun@email.de")
                .phone("+49 176 55443322")
                .birthDate(LocalDate.of(2000, 12, 20))
                .gender(Gender.FEMALE)
                .contractModel(ContractModel.STUDENT)
                .contractStart(LocalDate.of(2026, 3, 1))
                .status(MemberStatus.ACTIVE)
                .user(memberUser3)
                .build());

        Member paused = memberRepository.save(Member.builder()
                .memberNumber("M-2026-00004")
                .firstName("Klaus")
                .lastName("Weber")
                .email("klaus.weber@email.de")
                .phone("+49 170 12345678")
                .birthDate(LocalDate.of(1975, 6, 10))
                .gender(Gender.MALE)
                .contractModel(ContractModel.BASIC)
                .contractStart(LocalDate.of(2025, 6, 1))
                .status(MemberStatus.PAUSED)
                .build());

        Trainer sarah = trainerRepository.save(Trainer.builder()
                .firstName("Sarah")
                .lastName("Koch")
                .email("sarah.koch@fitnessstudio.de")
                .phone("+49 151 22334455")
                .active(true)
                .build());

        Trainer max = trainerRepository.save(Trainer.builder()
                .firstName("Max")
                .lastName("Fischer")
                .email("max.fischer@fitnessstudio.de")
                .phone("+49 162 33445566")
                .active(true)
                .build());

        Trainer julia = trainerRepository.save(Trainer.builder()
                .firstName("Julia")
                .lastName("Hoffmann")
                .email("julia.hoffmann@fitnessstudio.de")
                .phone("+49 173 44556677")
                .active(false) // inactive trainer
                .build());

        LocalDate today = LocalDate.now();

        Course yoga = courseRepository.save(Course.builder()
                .name("Morning Yoga Flow")
                .description("Sanfter Yoga-Kurs für den Start in den Tag. Geeignet für alle Levels.")
                .trainer(sarah)
                .category(CourseCategory.YOGA)
                .difficulty(CourseDifficulty.BEGINNER)
                .maxParticipants(15)
                .date(today.plusDays(1))
                .timeFrom(LocalTime.of(8, 0))
                .timeUntil(LocalTime.of(9, 0))
                .status(CourseStatus.ACTIVE)
                .build());

        Course strength = courseRepository.save(Course.builder()
                .name("Power Strength Training")
                .description("Intensives Krafttraining für Fortgeschrittene. Bitte vorher aufwärmen.")
                .trainer(max)
                .category(CourseCategory.STRENGTH)
                .difficulty(CourseDifficulty.EXPERT)
                .maxParticipants(10)
                .date(today.plusDays(2))
                .timeFrom(LocalTime.of(18, 0))
                .timeUntil(LocalTime.of(19, 30))
                .status(CourseStatus.ACTIVE)
                .build());

        Course cardio = courseRepository.save(Course.builder()
                .name("HIIT Cardio Blast")
                .description("High Intensity Interval Training – maximale Fettverbrennung in 45 Minuten.")
                .trainer(max)
                .category(CourseCategory.CARDIO)
                .difficulty(CourseDifficulty.INTERMEDIATE)
                .maxParticipants(20)
                .date(today.plusDays(3))
                .timeFrom(LocalTime.of(12, 0))
                .timeUntil(LocalTime.of(12, 45))
                .status(CourseStatus.ACTIVE)
                .build());

        Course dance = courseRepository.save(Course.builder()
                .name("Zumba Party")
                .description("Tanzen und Spaß haben – Latin-Dance-inspired Cardio für alle.")
                .trainer(sarah)
                .category(CourseCategory.DANCE)
                .difficulty(CourseDifficulty.BEGINNER)
                .maxParticipants(25)
                .date(today.plusDays(4))
                .timeFrom(LocalTime.of(17, 0))
                .timeUntil(LocalTime.of(18, 0))
                .status(CourseStatus.ACTIVE)
                .build());

        courseRepository.save(Course.builder()
                .name("Pilates Basics")
                .description("Dieser Kurs wurde leider abgesagt.")
                .trainer(julia)
                .category(CourseCategory.OTHER)
                .difficulty(CourseDifficulty.BEGINNER)
                .maxParticipants(12)
                .date(today.plusDays(5))
                .timeFrom(LocalTime.of(10, 0))
                .timeUntil(LocalTime.of(11, 0))
                .status(CourseStatus.CANCELLED)
                .build());

        registrationRepository.save(CourseRegistration.builder()
                .course(yoga)
                .member(anna)
                .status(RegistrationStatus.REGISTERED)
                .build());

        registrationRepository.save(CourseRegistration.builder()
                .course(strength)
                .member(anna)
                .status(RegistrationStatus.REGISTERED)
                .build());

        registrationRepository.save(CourseRegistration.builder()
                .course(cardio)
                .member(tom)
                .status(RegistrationStatus.REGISTERED)
                .build());

        registrationRepository.save(CourseRegistration.builder()
                .course(yoga)
                .member(tom)
                .status(RegistrationStatus.WAITLIST)
                .build());

        registrationRepository.save(CourseRegistration.builder()
                .course(dance)
                .member(lena)
                .status(RegistrationStatus.REGISTERED)
                .build());

        log.info("✓ Seeded: 5 users, 4 members, 3 trainers, 5 courses, 5 registrations.");
        log.info("─────────────────────────────────────────────");
        log.info("  Admin login : admin@fitnessstudio.de / admin123");
        log.info("  Staff login : empfang@fitnessstudio.de / staff123");
        log.info("  Member login: anna.mueller@email.de / member123");
        log.info("─────────────────────────────────────────────");
    }
}