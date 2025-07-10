package com.example.massagesystem.announcement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }

    public Announcement createAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    public Announcement updateAnnouncement(Long id, Announcement announcementDetails) {
        Announcement announcement = announcementRepository.findById(id).orElseThrow(() -> new RuntimeException("Announcement not found"));
        announcement.setTitle(announcementDetails.getTitle());
        announcement.setContent(announcementDetails.getContent());
        return announcementRepository.save(announcement);
    }

    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}
