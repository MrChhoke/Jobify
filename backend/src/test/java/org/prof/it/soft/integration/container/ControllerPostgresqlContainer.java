package org.prof.it.soft.integration.container;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.testcontainers.containers.PostgreSQLContainer;

public class ControllerPostgresqlContainer extends PostgreSQLContainer<ControllerPostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:16";
    private static ControllerPostgresqlContainer container;

    private ControllerPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static ControllerPostgresqlContainer getInstance() {
        if (container == null) {
            container = new ControllerPostgresqlContainer()
                    .withDatabaseName("testing_instance")
                    .withUsername("test")
                    .withPassword("test_password")
                    .withExposedPorts(5432) // Открываем порт 5432 внутри контейнера
                    .withCreateContainerCmdModifier(cmd -> cmd.getHostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5433), new ExposedPort(5432))));
            container.start();
        }
        return container;
    }

}
