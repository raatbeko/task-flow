package kg.core.project.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;


@Document(indexName = "projects")
@Setting(settingPath = "es-settings.json")
@Getter
@Setter
public class ProjectDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "my_ngram_analyzer", searchAnalyzer = "standard")
    private String name;

    @Field(type = FieldType.Text, analyzer = "my_ngram_analyzer", searchAnalyzer = "standard")
    private String description;


    @Field(type = FieldType.Keyword)
    private String ownerUsername;

    @Field(type = FieldType.Keyword)
    private ProjectStatus status;

}
