package alatoo.edu.kg.api.service.impl;

import alatoo.edu.kg.api.exception.NotFoundException;
import alatoo.edu.kg.api.exception.UnauthorizedActionException;
import alatoo.edu.kg.api.mapper.ReminderMapper;
import alatoo.edu.kg.api.payload.reminder.ReminderRequestDTO;
import alatoo.edu.kg.api.payload.reminder.ReminderResponseDTO;
import alatoo.edu.kg.api.service.ReminderService;
import alatoo.edu.kg.store.entity.Post;
import alatoo.edu.kg.store.entity.Reminder;
import alatoo.edu.kg.store.entity.User;
import alatoo.edu.kg.store.entity.UserDetailsImpl;
import alatoo.edu.kg.store.repository.PostRepository;
import alatoo.edu.kg.store.repository.ReminderRepository;

import alatoo.edu.kg.store.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReminderServiceImpl implements ReminderService {

    ReminderRepository reminderRepository;
    PostRepository postRepository;
    ReminderMapper reminderMapper;
    UserRepository userRepository;

    @Override
    @Transactional
    public ReminderResponseDTO createReminder(Long postId, ReminderRequestDTO dto) {
        User currentUser = getCurrentUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        Reminder reminder = reminderMapper.toEntity(dto);
        reminder.setUser(currentUser);
        reminder.setPost(post);

        Reminder savedReminder = reminderRepository.save(reminder);
        return reminderMapper.toDTO(savedReminder);
    }

    @Override
    @Transactional
    public List<ReminderResponseDTO> getUserReminders() {
        User currentUser = getCurrentUser();
        List<Reminder> reminders = reminderRepository.findByUserId(currentUser.getId());
        return reminders.stream()
                .map(reminderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReminder(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NotFoundException("Reminder not found with id: " + reminderId));

        User currentUser = getCurrentUser();
        if (!reminder.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You are not authorized to delete this reminder");
        }

        reminderRepository.deleteById(reminderId);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedActionException("User not authenticated");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findByIdWithFavoritePosts(userDetails.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
