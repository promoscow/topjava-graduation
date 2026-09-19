package ru.xpendence.topjavagraduation.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import ru.xpendence.topjavagraduation.entity.Tag
import ru.xpendence.topjavagraduation.repository.TagRepository
import ru.xpendence.topjavagraduation.service.TagService

@Service
class TagServiceImpl(
    private val repository: TagRepository,
) : TagService {

    @Transactional
    override fun create(tag: Tag): Tag {
        ensureNameIsFree(tag.name)
        return repository.save(tag)
    }

    @Transactional
    override fun update(tag: Tag) {
        val id = tag.id ?: throw IllegalArgumentException("Tag id is null")
        val stored = repository.findById(id)
            .orElseThrow { NoSuchElementException("Tag not found by id: $id") }
        if (tag.name != stored.name) {
            ensureNameIsFree(tag.name)
        }
        repository.save(Tag.enrichForUpdate(tag, stored))
    }

    @Transactional(readOnly = true)
    override fun getById(id: Long): Tag =
        repository.findById(id)
            .orElseThrow { NoSuchElementException("Tag not found by id: $id") }

    @Transactional(readOnly = true)
    override fun getAll(name: String?, pageable: Pageable): Page<Tag> =
        if (StringUtils.hasText(name)) {
            repository.findByNameContainingIgnoreCase(checkNotNull(name), pageable)
        } else {
            repository.findAll(pageable)
        }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    private fun ensureNameIsFree(name: String) {
        if (repository.existsByName(name)) {
            throw IllegalArgumentException("Tag name already taken: $name")
        }
    }
}
