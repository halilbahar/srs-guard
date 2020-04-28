package at.htl.srsguard.service;

import at.htl.srsguard.entity.App;
import at.htl.srsguard.entity.Permission;
import at.htl.srsguard.entity.Role;
import at.htl.srsguard.entity.Stream;
import at.htl.srsguard.model.AppStream;
import at.htl.srsguard.repository.AppRepository;
import at.htl.srsguard.repository.PermissionRepository;
import at.htl.srsguard.repository.RoleRepository;
import at.htl.srsguard.repository.StreamRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class RolePermissionService {

    @Inject
    PermissionRepository permissionRepository;
    @Inject
    AppRepository appRepository;
    @Inject
    StreamRepository streamRepository;
    @Inject
    RoleRepository roleRepository;

    @Transactional
    public void addPermissions(Role role, List<AppStream> appStreamList) {
        for (AppStream appStream : appStreamList) {
            String appName = appStream.getApp();
            String streamName = appStream.getStream();

            // Look for existing permission
            Permission existingPermission = this.permissionRepository.find(
                    "app.name = ?1 and stream.name = ?2", appName, streamName
            ).firstResult();

            // If you find an existing permission use that
            if (existingPermission != null) {
                role.getPermissionList().add(existingPermission);
                continue;
            }

            // Try to find if the app and stream already exist in the database
            App requiredApp = this.appRepository.find("name", appName).firstResult();
            Stream requiredStream = this.streamRepository.find("name", streamName).firstResult();

            // If they dont exist, create them
            if (requiredApp == null) requiredApp = new App(appName);
            if (requiredStream == null) requiredStream = new Stream(streamName);

            Permission permission = new Permission(requiredApp, requiredStream);
            this.permissionRepository.persist(permission);
            role.getPermissionList().add(permission);
        }
    }

    @Transactional
    public void removePermissions(Long id, List<AppStream> appStreamList) {
        Role role = this.roleRepository.findById(id);
        List<Permission> permissions = role.getPermissionList();
        List<Permission> toBeDeletedPermissions = new LinkedList<>();

        for (AppStream appStream : appStreamList) {
            for (Permission permission : permissions) {
                // Collect the permission if you match app and stream
                if (appStream.equals(permission)) {
                    toBeDeletedPermissions.add(permission);
                }
            }
        }

        permissions.removeAll(toBeDeletedPermissions);
        this.cleanUpDatabase();
    }

    public List<AppStream> getCommonPermissions(Role role, List<AppStream> appStreamList) {
        return appStreamList.stream()
                .filter(appStream -> {
                    for (Permission permission : role.getPermissionList()) {
                        if (appStream.equals(permission)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public Set<AppStream> getDuplicates(List<AppStream> appStreamList) {
        Set<AppStream> duplicates = new LinkedHashSet<>();
        Set<AppStream> uniques = new HashSet<>();

        for (AppStream appStream : appStreamList) {
            if (!uniques.add(appStream)) {
                duplicates.add(appStream);
            }
        }

        return duplicates;
    }

    @Transactional
    private void cleanUpDatabase() {
        // Delete all the permissions who don't belong to any role
        List<Permission> orphanPermissions = this.permissionRepository.find("size(roles) = 0").list();
        orphanPermissions.forEach(this.permissionRepository::delete);
        // Delete all the app who don't belong to any permission
        List<App> orphanApps = this.appRepository.find("size(permissions) = 0").list();
        orphanApps.forEach(this.appRepository::delete);
        // Delete all the streams who don't belong to any permission
        List<Stream> orphanStreams = this.streamRepository.find("size(permissions) = 0").list();
        orphanStreams.forEach(this.streamRepository::delete);
    }
}
