package kg.core.task.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;


@Document(indexName = "tasks")
@Setting(settingPath = "es-settings.json")
@Getter
@Setter
public class TaskDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "my_ngram_analyzer", searchAnalyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "my_ngram_analyzer", searchAnalyzer = "standard")
    private String description;
}