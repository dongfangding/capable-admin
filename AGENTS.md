# capable-admin

## SQL Mapper Rules

- Never modify any SQL statement in `/src/main/resources/mapper/*Mapper.xml`.
- Never add new SQL statements to `/src/main/resources/mapper/*Mapper.xml`.
- If SQL needs to be added or changed, create a new mapper XML under `/src/main/resources/mapper/ext`.
- New custom mapper XML files must use the `*ExtMapper.xml` naming format, for example `SysUserExtMapper.xml`.
- New custom mapper Java interfaces must be created under the corresponding `.../infra/mapper/ext/` package and use the `*ExtMapper.java` naming format.
- All custom SQL must be implemented in the `ext` mapper pair only. Do not write custom SQL into the original generated mapper XML files.
- If an existing feature currently depends on an original mapper, keep the original mapper untouched and switch service or application code to call the new ext mapper instead.
- Before editing any mapper XML, first verify whether the target file is under `/src/main/resources/mapper/ext`. If not, stop and use the ext mapper pattern.
