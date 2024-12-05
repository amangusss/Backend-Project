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
import alatoo.edu.kg.store.repository.PostRepository;
import alatoo.edu.kg.store.repository.ReminderRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    @Override
    public ReminderResponseDTO createReminder(Long postId, ReminderRequestDTO dto) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found with id: " + postId));

        Reminder reminder = reminderMapper.toEntity(dto);
        reminder.setUser(currentUser);
        reminder.setPost(post);

        Reminder savedReminder = reminderRepository.save(reminder);
        return reminderMapper.toDTO(savedReminder);
    }

    @Override
    public List<ReminderResponseDTO> getUserReminders() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reminder> reminders = reminderRepository.findByUserId(currentUser.getId());
        return reminders.stream()
                .map(reminderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReminder(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new NotFoundException("Reminder not found with id: " + reminderId));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!reminder.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You are not authorized to delete this reminder");
        }

        reminderRepository.deleteById(reminderId);
    }
}
