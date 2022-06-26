package com.spe.iiitbcms.service;

import com.spe.iiitbcms.dto.SubpostDto;
import com.spe.iiitbcms.exceptions.CMSException;
import com.spe.iiitbcms.mapper.SubpostMapper;
import com.spe.iiitbcms.model.Subpost;
import com.spe.iiitbcms.repository.SubpostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SubpostService {

    private final SubpostRepository subpostRepository;
    private final SubpostMapper subpostMapper;

    @Transactional
    public Subpost save(SubpostDto subpostDto)
    {
        Subpost subpost = new Subpost();

        subpost.setDescription(subpostDto.getDescription());
        subpost.setName(subpostDto.getName());
        subpost.setCreatedDate(Instant.now());

        subpostRepository.save(subpost);

        return subpost;
    }

    public List<Subpost> getAll() {
        return subpostRepository.findAll();
    }

    public SubpostDto getSubpost(Long id) {
        Subpost subpost = subpostRepository.findById(id)
                .orElseThrow(() -> new CMSException("No subpost found with ID - " + id));
        return subpostMapper.mapSubpostToDto(subpost);
    }

    public boolean deleteSubPost(String role) {
        Subpost subpost = subpostRepository.getByName(role);
        if(subpost!=null) {
            subpostRepository.delete(subpost);
            return true;
        }
        return false;

    }
}
