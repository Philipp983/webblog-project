package de.brightslearning.webblog.blogentry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogEntryRepository extends JpaRepository<BlogEntry, Integer> {
}
