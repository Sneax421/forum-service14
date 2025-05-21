package ait.cohort55.accounting.service;

import ait.cohort55.accounting.dao.UserAccountRepository;
import ait.cohort55.accounting.dto.RolesDto;
import ait.cohort55.accounting.dto.UserDto;
import ait.cohort55.accounting.dto.UserEditDto;
import ait.cohort55.accounting.dto.UserRegisterDto;
import ait.cohort55.accounting.dto.exception.UserExistsException;
import ait.cohort55.accounting.dto.exception.UserNotFoundException;
import ait.cohort55.accounting.model.Role;
import ait.cohort55.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountingService {

    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if(userAccountRepository.existsById(userRegisterDto.getLogin())){
            throw new UserExistsException();
        }
        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        String password = BCrypt.hashpw(userRegisterDto.getPassword(), BCrypt.gensalt());
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto removeUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String firstName = userEditDto.getFirstName();
        if(firstName != null){
            userAccount.setFirstName(firstName);
        }
        String lastName = userEditDto.getLastName();
        if(lastName != null){
            userAccount.setLastName(lastName);
        }
        userAccount = userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        if(userAccount.getRoles() == null){
            userAccount.addRole(role);
        }else {
            userAccount.removeRole(role);
        }
        return modelMapper.map(userAccount, RolesDto.class);

    }

    @Override
    public void changePassword(String login, String newPassword) {

    }
}
