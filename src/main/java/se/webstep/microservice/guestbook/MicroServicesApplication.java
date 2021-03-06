package se.webstep.microservice.guestbook;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.lifecycle.setup.ExecutorServiceBuilder;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.skife.jdbi.v2.DBI;
import se.webstep.microservice.guestbook.health.SimpleHealthCheck;
import se.webstep.microservice.guestbook.resource.DocumentationResource;
import se.webstep.microservice.guestbook.resource.EntriesResource;
import se.webstep.microservice.guestbook.resource.EntryResource;
import se.webstep.microservice.guestbook.resource.GuestbookResource;

public class MicroServicesApplication extends Application<MicroServicesConfig> {

    private DBI jdbi;

    public static void main(String[] args) throws Exception {
        new MicroServicesApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<MicroServicesConfig> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<MicroServicesConfig>() {
            @Override
            public DataSourceFactory getDataSourceFactory(MicroServicesConfig configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(new SwaggerBundle<MicroServicesConfig>() {
            @Override
            public SwaggerBundleConfiguration getSwaggerBundleConfiguration(MicroServicesConfig configuration) {
                return new SwaggerBundleConfiguration("localhost", 8080);
            }
        });
    }

    @Override
    public void run(MicroServicesConfig microServicesConfig, Environment environment) throws Exception {
        jdbi = new DBIFactory().build(environment, microServicesConfig.getDataSourceFactory(), "guestbook");

        environment.jersey().register(new DocumentationResource(getName()));
        environment.jersey().register(new GuestbookResource(this));
        environment.jersey().register(new EntriesResource(this));
        environment.jersey().register(new EntryResource(this));
        environment.healthChecks().register("simple", new SimpleHealthCheck());
    }

    @Override
    public String getName() {
        return "Guestbook service";
    }

    public DBI getJdbi() {
        return jdbi;
    }

}
