package bw.co.knowvera.form.field;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormFieldRepository extends JpaRepository<FormField, UUID> {

}
